package com.teste.banco.banco.Services;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String saudacao(String nome) {
        return "Olá, " + nome + "! Bem-vindo ao sistema.";
    }
}