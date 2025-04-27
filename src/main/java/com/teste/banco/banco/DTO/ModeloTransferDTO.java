package com.teste.banco.banco.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModeloTransferDTO {
    
    private int numeroContaDestino;
    private Double valorTransferencia;
    private String cpfDestino;
    private String cpfOrigem;
    private int numeroContaOrigem;
}
