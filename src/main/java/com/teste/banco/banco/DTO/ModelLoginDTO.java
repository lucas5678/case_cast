package com.teste.banco.banco.DTO;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelLoginDTO {
    
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    private String perfil; 
    private String nome;
    private String titular;
    private int numeroConta;
    private double saldo;
    private int numeroContaDestino;
    private double valorTransferencia;
}
