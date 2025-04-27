package com.teste.banco.banco.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.teste.banco.banco.DTO.ModelLoginDTO;
import com.teste.banco.banco.Services.ContaLogin;

import jakarta.validation.Valid;

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
    public String salvarConta(
            @Valid @ModelAttribute ModelLoginDTO usuario,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            System.out.println("Erro de validação: " + result.getAllErrors());
            redirectAttributes.addFlashAttribute("erro", "Preencha todos os campos corretamente!");
            return "redirect:/";
        }

        try {
            contaLogin.logar(usuario);
            redirectAttributes.addFlashAttribute("mensagem", "Conta cadastrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/";
        }
        return "redirect:/admin";
    }

}
