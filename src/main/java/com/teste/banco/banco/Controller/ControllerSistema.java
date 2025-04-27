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

import jakarta.servlet.http.HttpSession;
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
    public String logarUsuario(
            @Valid @ModelAttribute ModelLoginDTO usuario,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        if (result.hasErrors()) {
            System.out.println("Erro de validação: " + result.getAllErrors());
            redirectAttributes.addFlashAttribute("erro", "Preencha todos os campos corretamente!");
            return "redirect:/";
        }

        try {
            ModelLoginDTO login = contaLogin.logar(usuario);

            if ("Admin".equalsIgnoreCase(login.getPerfil())) {
                session.setAttribute("conta", login);
                return "redirect:/admin";
            } else if ("user".equalsIgnoreCase(login.getPerfil())) {
                session.setAttribute("conta", login);
                return "redirect:/user";
            } else {
                redirectAttributes.addFlashAttribute("erro", "Perfil desconhecido!");
                return "redirect:/";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/"; 
    }

}
