package com.teste.banco.banco.strategy;

import com.teste.banco.banco.Models.ModelConta;

public class MovimentacaoContext {
    private MovimentacaoStrategy strategy;

    public MovimentacaoContext(MovimentacaoStrategy strategy) {
        this.strategy = strategy;
    }

    public ModelConta executar(ModelConta conta) {
        return strategy.executar(conta);
    }
}