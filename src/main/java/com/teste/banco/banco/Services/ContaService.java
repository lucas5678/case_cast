package com.teste.banco.banco.Services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.banco.banco.DTO.ModelContaDTO;
import com.teste.banco.banco.Models.ModelConta;
import com.teste.banco.banco.Repository.ContaRepository;
import com.teste.banco.banco.exceptions.ContaException;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public ModelConta salvarConta(ModelContaDTO contaDTO) {
        validarCpf(contaDTO.getCpf());
        validarNumeroConta(contaDTO.getNumeroConta());
        validarSaldo(contaDTO.getSaldo());
        return contaRepository.save(contaDTO.toEntity());
    }

    private void validarCpf(String cpf) {
        if (contaRepository.existsByCpf(cpf)) {
            throw new ContaException("CPF já cadastrado!");
        }
    }

    private void validarNumeroConta(Integer numeroConta) {
        if (contaRepository.existsByNumeroConta(numeroConta)) {
            throw new ContaException("Número da conta já existe!");
        }
    }

    private void validarSaldo(double saldo) {
        if (saldo < 0) {
            throw new ContaException("Saldo não pode ser negativo!");
        }
    }

    public Integer gerarNumeroConta() {
        return Integer.parseInt(UUID.randomUUID().toString().substring(0, 8).replaceAll("[^\\d]", ""));
    }
}
