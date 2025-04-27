package com.teste.banco.banco.DTO;
import com.teste.banco.banco.Models.ModelConta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelContaDTO {
    
    private String titular;
    private String cpf;
    private int numeroConta;
    private Double saldo;

    public ModelConta toEntity() {
        ModelConta conta = new ModelConta();
        conta.setTitular(this.titular);
        conta.setCpf(this.cpf);
        conta.setNumeroConta(this.numeroConta);
        conta.setSaldo(this.saldo);
        return conta;
    }
}
