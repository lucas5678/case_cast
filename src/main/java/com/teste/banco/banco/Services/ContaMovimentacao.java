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
    @Autowired
    private ContaRepository contaRepository;
    
    public Optional<ModelConta> buscarPorNumero(Integer numeroConta) {
        return contaRepository.findByNumeroConta(numeroConta);
    }

    @Transactional
    public ModelConta AdicionarCredito(ModelContaDTO conta) {
        Optional<ModelConta> optionalConta = contaRepository.findByNumeroContaUpdate(conta.getNumeroConta());
        if(conta.getSaldo() < 0) {
            throw new ContaException("Valor de crédito não pode ser negativo!");
        }
        if (optionalConta.isPresent()) {
            ModelConta contaExistente = optionalConta.get();
            // Adicione o valor do crédito ao saldo existente
            contaExistente.setSaldo(contaExistente.getSaldo() + conta.getSaldo());
            return contaRepository.save(contaExistente);
        } 
        else {
            throw new ContaException("Conta não encontrada!");
        }
    }

    @Transactional
    public ModelConta DebitarCredito(ModelConta conta) {
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
