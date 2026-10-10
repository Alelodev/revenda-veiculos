package br.com.revenda.controller;

import br.com.revenda.model.Proprietario;
import br.com.revenda.service.ProprietarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.revenda.exception.RecursoNaoEncontradoException;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class ProprietarioController {

    private ProprietarioService proprietarioService;

    public ProprietarioController(ProprietarioService proprietarioService) {
        this.proprietarioService = proprietarioService;
    }

    @GetMapping("/proprietarios")
    @ResponseBody
    public ResponseEntity<List<Proprietario>> listarProprietarios() {
        List<Proprietario> mostarProprietarios = proprietarioService.mostrarTodos();
        return ResponseEntity.ok(mostarProprietarios);
    }

    @GetMapping("/proprietarios/{id}")
    @ResponseBody
    public ResponseEntity<Proprietario> buscaPorId(@PathVariable Long id) {
        Proprietario proprietario = proprietarioService.buscarId(id);
        return ResponseEntity.ok(proprietario);
    }

    @PostMapping("/proprietarios")
    @ResponseBody
    public ResponseEntity<Proprietario> cadastrar(@Valid @RequestBody Proprietario proprietario) {
        Proprietario proprietarioSalvo = proprietarioService.salvar(proprietario);

        return ResponseEntity.status(201).body(proprietarioSalvo);
    }


    @PutMapping("/proprietarios/{id}")
    @ResponseBody
    public ResponseEntity<Proprietario> atualizar(@PathVariable Long id, @Valid @RequestBody Proprietario proprietario) {
        Proprietario proprietarioAtualizado = proprietarioService.atualizar(id, proprietario);
        return ResponseEntity.ok(proprietarioAtualizado);
    }

    @DeleteMapping("/proprietarios/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        proprietarioService.deletarId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/proprietarios/tela")
    public String telaProprietarios(Model model) {

        model.addAttribute(
                "proprietarios",
                proprietarioService.mostrarTodos()
        );

        return "proprietarios";
    }

    @GetMapping("/proprietarios/novo")
    public String novoProprietario() {
        return "cadastro-proprietario";
    }

    @PostMapping("/proprietarios/form")
    public String cadastrarFormulario(
            @ModelAttribute Proprietario proprietario
    ) {

        proprietarioService.salvar(proprietario);

        return "redirect:/proprietarios/tela";
    }



    @PostMapping("/proprietarios/{id}/excluir")
    public String excluirFormulario(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            proprietarioService.deletarId(id);
            redirectAttributes.addFlashAttribute("sucesso", "Cadastro excluído com sucesso.");
        } catch (RecursoNaoEncontradoException exception) {
            redirectAttributes.addFlashAttribute("erro", "O proprietário não foi encontrado.");
        } catch (DataIntegrityViolationException exception) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não foi possível excluir o proprietário. Verifique se existem veículos vinculados."
            );
        }
        return "redirect:/proprietarios/tela";
    }
}

