package com.teste.banco.banco.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.teste.banco.banco.DTO.ModelLoginDTO;
import com.teste.banco.banco.Services.ContaLogin;

@Controller
public class ControllerSistema {

    @Autowired
    private ContaLogin contaLogin;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("usuario", new ModelLoginDTO());
        return "index";
    }

    @PostMapping("/logar")
    public String salvarConta(@ModelAttribute ModelLoginDTO usuario, RedirectAttributes redirectAttributes) {
        try {
            contaLogin.logar(usuario);
            redirectAttributes.addFlashAttribute("mensagem", "Conta cadastrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin";
    }
}
