package br.com.revenda.controller;

import br.com.revenda.exception.RecursoNaoEncontradoException;
import br.com.revenda.model.Documento;
import br.com.revenda.model.Usuario;
import br.com.revenda.model.Veiculo;
import br.com.revenda.service.DocumentoService;
import br.com.revenda.service.UsuarioService;
import br.com.revenda.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;

@Controller
public class DocumentoController {

    private final DocumentoService documentoService;
    private final UsuarioService usuarioService;
    private final VeiculoService veiculoService;

    public DocumentoController(
            DocumentoService documentoService,
            UsuarioService usuarioService,
            VeiculoService veiculoService
    ) {
        this.documentoService = documentoService;
        this.usuarioService = usuarioService;
        this.veiculoService = veiculoService;
    }

    @GetMapping("/documentos")
    @ResponseBody
    public ResponseEntity<List<Documento>> listarDocumentos(
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        List<Documento> documentos;

        if (usuario.getPerfil().equals("ADMIN")) {

            documentos =
                    documentoService.mostrarTodos();

        } else {

            documentos =
                    documentoService
                            .mostrarTodos()
                            .stream()
                            .filter(documento ->
                                    documento.getVeiculo() != null
                                            &&
                                            documento.getVeiculo()
                                                    .getUsuario() != null
                                            &&
                                            documento.getVeiculo()
                                                    .getUsuario()
                                                    .getIdUsuario()
                                                    .equals(
                                                            usuario.getIdUsuario()
                                                    )
                            )
                            .toList();
        }

        return ResponseEntity.ok(documentos);
    }


    @GetMapping("/documentos/{id}")
    @ResponseBody
    public ResponseEntity<Documento> buscarPorId(
            @PathVariable Long id,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        Documento documento =
                documentoService.buscarDocumentoPermitido(
                        id,
                        usuario
                );

        return ResponseEntity.ok(documento);
    }


    @PostMapping("/documentos")
    @ResponseBody
    public ResponseEntity<Documento> cadastrar(
            @Valid @RequestBody Documento documento,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        Long idVeiculo =
                documento
                        .getVeiculo()
                        .getIdVeiculo();

        Veiculo veiculo =
                veiculoService.buscarVeiculoPermitido(
                        idVeiculo,
                        usuario
                );

        documento.setVeiculo(veiculo);

        Documento documentoSalvo =
                documentoService.salvar(documento);

        return ResponseEntity
                .status(201)
                .body(documentoSalvo);
    }


    @PutMapping("/documentos/{id}")
    @ResponseBody
    public ResponseEntity<Documento> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Documento documento,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        Documento documentoExistente =
                documentoService.buscarDocumentoPermitido(
                        id,
                        usuario
                );

        // Impede mudar o documento para outro veículo
        documento.setVeiculo(
                documentoExistente.getVeiculo()
        );

        Documento documentoAtualizado =
                documentoService.atualizar(
                        id,
                        documento
                );

        return ResponseEntity.ok(
                documentoAtualizado
        );
    }


    @DeleteMapping("/documentos/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        Documento documento =
                documentoService.buscarDocumentoPermitido(
                        id,
                        usuario
                );

        documentoService.deletarId(
                documento.getIdDocumento()
        );

        return ResponseEntity
                .noContent()
                .build();
    }


    @PostMapping("/documentos/upload")
    @ResponseBody
    public ResponseEntity<Documento> upload(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("idVeiculo") Long idVeiculo,
            @RequestParam("tipoDocumento") String tipoDocumento,
            Principal principal
    ) throws IOException {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        veiculoService.buscarVeiculoPermitido(
                idVeiculo,
                usuario
        );

        Documento documentoSalvo =
                documentoService.salvarArquivo(
                        arquivo,
                        idVeiculo,
                        tipoDocumento
                );

        return ResponseEntity
                .status(201)
                .body(documentoSalvo);
    }

    @GetMapping("/documentos/{id}/download")
    @ResponseBody
    public ResponseEntity<Resource> download(
            @PathVariable Long id,
            Principal principal
    ) throws IOException {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        Documento documento =
                documentoService.buscarDocumentoPermitido(
                        id,
                        usuario
                );

        Resource recurso =
                documentoService.baixarArquivo(id);

        String tipoConteudo =
                Files.probeContentType(
                        Path.of(
                                documento.getCaminhoArquivo()
                        )
                );

        if (tipoConteudo == null) {
            tipoConteudo =
                    "application/octet-stream";
        }

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                documento.getNome() +
                                "\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                tipoConteudo
                        )
                )
                .body(recurso);
    }

    @PostMapping("/documentos/upload-form")
    public String uploadFormulario(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("idVeiculo") Long idVeiculo,
            @RequestParam("tipoDocumento") String tipoDocumento,
            Principal principal
    ) throws IOException {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        veiculoService.buscarVeiculoPermitido(
                idVeiculo,
                usuario
        );

        documentoService.salvarArquivo(
                arquivo,
                idVeiculo,
                tipoDocumento
        );

        return "redirect:/veiculos/" +
                idVeiculo +
                "/documentos";
    }

    @PostMapping("/veiculos/{idVeiculo}/documentos/{idDocumento}/excluir")
    public String excluirFormulario(
            @PathVariable Long idVeiculo,
            @PathVariable Long idDocumento,
            RedirectAttributes redirectAttributes,
            Principal principal
    ) {

        try {

            Usuario usuario =
                    usuarioService.buscarPorLogin(
                            principal.getName()
                    );

            Documento documento =
                    documentoService.buscarDocumentoPermitido(
                            idDocumento,
                            usuario
                    );

            if (
                    documento.getVeiculo() == null
                            ||
                            !idVeiculo.equals(
                                    documento
                                            .getVeiculo()
                                            .getIdVeiculo()
                            )
            ) {

                redirectAttributes.addFlashAttribute(
                        "erro",
                        "Documento não encontrado."
                );

            } else {

                documentoService.deletarId(
                        idDocumento
                );

                redirectAttributes.addFlashAttribute(
                        "sucesso",
                        "Registro do documento excluído com sucesso."
                );
            }

        } catch (RecursoNaoEncontradoException exception) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Documento não encontrado."
            );

        } catch (DataIntegrityViolationException exception) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não foi possível excluir o documento."
            );
        }

        return "redirect:/veiculos/" +
                idVeiculo +
                "/documentos";
    }
}