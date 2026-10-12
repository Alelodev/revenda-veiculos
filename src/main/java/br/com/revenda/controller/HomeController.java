package br.com.revenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String loginInicial() {
        return "redirect:/login";
    }

    @GetMapping("/principal")
    public String principal() {
        return "index";
    }
}