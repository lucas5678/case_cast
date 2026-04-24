package com.teste.banco.banco.strategy;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.teste.banco.banco.Models.ModelConta;
import com.teste.banco.banco.Repository.ContaRepository;
import com.teste.banco.banco.exceptions.ContaException;

@Component
public class DebitoStrategy implements MovimentacaoStrategy {
    
    private final ContaRepository contaRepository;
    
    public DebitoStrategy(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }
    
    @Override
    @Transactional
    public ModelConta executar(ModelConta conta) {
        Optional<ModelConta> optionalConta = contaRepository.findByNumeroContaUpdate(conta.getNumeroConta());
        if(conta.getSaldo() < 0) {
            throw new ContaException("Valor de crédito não pode ser negativo!");
        }
        if (optionalConta.isPresent()) {
            ModelConta contaExistente = optionalConta.get();
            
            //debita do valor existente o valor do credito
            contaExistente.setSaldo(contaExistente.getSaldo() - conta.getSaldo());
            if(contaExistente.getSaldo() < 0) {
                throw new ContaException("Saldo insuficiente!");
            }
            return contaRepository.save(contaExistente);
        } 
        else {
            throw new ContaException("Conta não encontrada!");
        }
    }
}