package com.teste.banco.banco.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelLoginDTO {
    
    private String cpf;
    private String nome;
    private String senha;
    private String perfil;
}
