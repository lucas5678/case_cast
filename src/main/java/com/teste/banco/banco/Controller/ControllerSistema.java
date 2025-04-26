package com.teste.banco.banco.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerSistema {
    
    @GetMapping("/")
    public String home() {
        return "index"; 
}
}
