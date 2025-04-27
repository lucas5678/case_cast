package com.teste.banco.banco.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.banco.banco.DTO.ModeloTransferDTO;
import com.teste.banco.banco.Repository.ContaRepository;

import jakarta.transaction.Transactional;

@Service
public class ContaTransfer {
    
    @Autowired
    ContaRepository contaRepository;

    @Transactional
    public ModeloTransferDTO transferir(ModeloTransferDTO transferencia) {
        validarValorTransferencia(transferencia);
        validarContasDiferentes(transferencia);
        var contaOrigem = contaRepository.findByNumeroContaUpdate(transferencia.getNumeroContaOrigem());
        validarContaExiste(contaOrigem, "Conta de origem não encontrada!");
        validarCpfOrigem(contaOrigem.get().getCpf(), transferencia.getCpfOrigem());
        var contaDestino = contaRepository.findByNumeroContaUpdate(transferencia.getNumeroContaDestino());
        validarContaExiste(contaDestino, "Conta de destino não encontrada!");
        validarSaldoSuficiente(contaOrigem.get().getSaldo(), transferencia.getValorTransferencia());

        // Realiza a transferência
        contaOrigem.get().setSaldo(contaOrigem.get().getSaldo() - transferencia.getValorTransferencia());
        contaDestino.get().setSaldo(contaDestino.get().getSaldo() + transferencia.getValorTransferencia());

        // Salva as alterações no banco de dados
        contaRepository.save(contaOrigem.get());
        contaRepository.save(contaDestino.get());

        return transferencia;
    }

    private void validarValorTransferencia(ModeloTransferDTO transferencia) {
        if (transferencia.getValorTransferencia() <= 0) {
            throw new RuntimeException("Valor de transferência deve ser maior que zero!");
        }
    }

    private void validarContasDiferentes(ModeloTransferDTO transferencia) {
        if (transferencia.getNumeroContaOrigem() == transferencia.getNumeroContaDestino()) {
            throw new RuntimeException("A conta de origem e destino não podem ser iguais!");
        }
    }

    private void validarContaExiste(java.util.Optional<?> conta, String mensagem) {
        if (conta.isEmpty()) {
            throw new RuntimeException(mensagem);
        }
    }

    private void validarCpfOrigem(String cpfConta, String cpfOrigem) {
        if (!cpfConta.equals(cpfOrigem)) {
            throw new RuntimeException("O CPF da conta de origem não confere!");
        }
    }

    private void validarSaldoSuficiente(double saldo, double valorTransferencia) {
        if (saldo < valorTransferencia) {
            throw new RuntimeException("Saldo insuficiente na conta de origem!");
        }
    }
}
