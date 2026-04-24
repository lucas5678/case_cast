package com.teste.banco.banco.strategy;

import com.teste.banco.banco.Models.ModelConta;

public interface MovimentacaoStrategy {
    ModelConta executar(ModelConta conta);
}