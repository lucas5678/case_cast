package com.teste.banco.banco.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teste.banco.banco.DTO.ModelContaDTO;
import com.teste.banco.banco.Models.ModelConta;
import com.teste.banco.banco.Repository.ContaRepository;
import com.teste.banco.banco.exceptions.ContaException;

@Service
public class ContaMovimentacao {
    
    private static final Double SALDO_MINIMO = 0.0;
    
    @Autowired
    private ContaRepository contaRepository;
    
    public Optional<ModelConta> buscarPorNumero(Integer numeroConta) {
        return contaRepository.findByNumeroConta(numeroConta);
    }

    @Transactional
    public ModelConta AdicionarCredito(ModelContaDTO conta) {
        Optional<ModelConta> optionalConta = contaRepository.findByNumeroContaUpdate(conta.getNumeroConta());
        if(conta.getSaldo() < 0) {
            throw new ContaException("Valor de crÃ©dito nÃ£o pode ser negativo!");
        }
        if (optionalConta.isPresent()) {
            ModelConta contaExistente = optionalConta.get();
            // Adicione o valor do crÃ©dito ao saldo existente
            contaExistente.setSaldo(contaExistente.getSaldo() + conta.getSaldo());
            return contaRepository.save(contaExistente);
        } 
        else {
            throw new ContaException("Conta nÃ£o encontrada!");
        }
    }

    @Transactional
    public ModelConta DebitarCredito(ModelConta conta) {
        if (conta.getSaldo() < 0) {
            throw new ContaException("Valor de débito não pode ser negativo!");
        }
        ModelConta contaExistente = contaRepository
            .findByNumeroContaUpdate(conta.getNumeroConta())
            .orElseThrow(() -> new ContaException("Conta não encontrada!"));

        double novoSaldo = contaExistente.getSaldo() - conta.getSaldo();
        if (novoSaldo < SALDO_MINIMO) {
            throw new ContaException("Saldo insuficiente!");
        }
        contaExistente.setSaldo(novoSaldo);
        return contaRepository.save(contaExistente);
    }
}
