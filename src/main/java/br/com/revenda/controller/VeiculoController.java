package br.com.revenda.controller;

import br.com.revenda.exception.RecursoNaoEncontradoException;
import br.com.revenda.model.Documento;
import br.com.revenda.model.Usuario;
import br.com.revenda.model.Veiculo;
import br.com.revenda.service.DocumentoService;
import br.com.revenda.service.ProprietarioService;
import br.com.revenda.service.UsuarioService;
import br.com.revenda.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class VeiculoController {

    private final VeiculoService veiculoService;
    private final DocumentoService documentoService;
    private final ProprietarioService proprietarioService;
    private final UsuarioService usuarioService;

    public VeiculoController(
            VeiculoService veiculoService,
            DocumentoService documentoService,
            ProprietarioService proprietarioService,
            UsuarioService usuarioService
    ) {
        this.veiculoService = veiculoService;
        this.documentoService = documentoService;
        this.proprietarioService = proprietarioService;
        this.usuarioService = usuarioService;
    }
    @GetMapping("/veiculos")
    @ResponseBody
    public ResponseEntity<List<Veiculo>> listarVeiculos(
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        List<Veiculo> veiculos;

        if (usuario.getPerfil().equals("ADMIN")) {
            veiculos = veiculoService.mostrarTodos();
        } else {
            veiculos = veiculoService.buscarPorUsuario(
                    usuario.getIdUsuario()
            );
        }

        return ResponseEntity.ok(veiculos);
    }


    @GetMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<Veiculo> buscarPorId(
            @PathVariable Long id,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        Veiculo veiculo =
                veiculoService.buscarVeiculoPermitido(
                        id,
                        usuario
                );

        return ResponseEntity.ok(veiculo);
    }


    @PostMapping("/veiculos")
    @ResponseBody
    public ResponseEntity<Veiculo> cadastrar(
            @Valid @RequestBody Veiculo veiculo,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        veiculo.setUsuario(usuario);

        Veiculo veiculoSalvo =
                veiculoService.salvar(veiculo);

        return ResponseEntity
                .status(201)
                .body(veiculoSalvo);
    }


    @PutMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<Veiculo> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Veiculo veiculo,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        Veiculo veiculoExistente =
                veiculoService.buscarVeiculoPermitido(
                        id,
                        usuario
                );

        veiculo.setUsuario(
                veiculoExistente.getUsuario()
        );

        Veiculo veiculoAtualizado =
                veiculoService.atualizar(
                        id,
                        veiculo
                );

        return ResponseEntity.ok(
                veiculoAtualizado
        );
    }


    @DeleteMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        Veiculo veiculo =
                veiculoService.buscarVeiculoPermitido(
                        id,
                        usuario
                );

        veiculoService.deletarId(
                veiculo.getIdVeiculo()
        );

        return ResponseEntity
                .noContent()
                .build();
    }



    @GetMapping("/veiculos/tela")
    public String telaVeiculos(
            Model model,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        List<Veiculo> veiculos;

        if (usuario.getPerfil().equals("ADMIN")) {

            veiculos =
                    veiculoService.mostrarTodos();

        } else {

            veiculos =
                    veiculoService.buscarPorUsuario(
                            usuario.getIdUsuario()
                    );
        }

        model.addAttribute(
                "veiculos",
                veiculos
        );

        return "veiculos";
    }


    @GetMapping("/veiculos/novo")
    public String novoVeiculo(
            Model model
    ) {

        model.addAttribute(
                "proprietarios",
                proprietarioService.mostrarTodos()
        );

        return "cadastro-veiculo";
    }


    @PostMapping("/veiculos/form")
    public String cadastrarFormulario(
            @ModelAttribute Veiculo veiculo,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        veiculo.setUsuario(usuario);

        veiculoService.salvar(veiculo);

        return "redirect:/veiculos/tela";
    }


    @GetMapping("/veiculos/{id}/documentos")
    public String telaDocumentosVeiculo(
            @PathVariable Long id,
            Model model,
            Principal principal
    ) {

        Usuario usuario =
                usuarioService.buscarPorLogin(principal.getName());

        Veiculo veiculo =
                veiculoService.buscarVeiculoPermitido(
                        id,
                        usuario
                );

        List<Documento> documentos =
                documentoService.buscarPorVeiculo(id);

        model.addAttribute(
                "veiculo",
                veiculo
        );

        model.addAttribute(
                "documentos",
                documentos
        );

        return "documentos-veiculo";
    }


    @PostMapping("/veiculos/{id}/excluir")
    public String excluirFormulario(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Principal principal
    ) {

        try {

            Usuario usuario =
                    usuarioService.buscarPorLogin(
                            principal.getName()
                    );

            Veiculo veiculo =
                    veiculoService.buscarVeiculoPermitido(
                            id,
                            usuario
                    );

            veiculoService.deletarId(
                    veiculo.getIdVeiculo()
            );

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Cadastro excluído com sucesso."
            );

        } catch (RecursoNaoEncontradoException exception) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "O veículo não foi encontrado."
            );

        } catch (DataIntegrityViolationException exception) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não foi possível excluir o veículo. Verifique se existem documentos vinculados."
            );
        }

        return "redirect:/veiculos/tela";
    }
}