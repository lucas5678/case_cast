package com.teste.banco.banco.Services;

import com.teste.banco.banco.DTO.ModeloTransferDTO;
import com.teste.banco.banco.Repository.ContaRepository;
import com.teste.banco.banco.Models.ModelConta; // Adicione esta linha
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContaTransferTest {

    private ContaRepository contaRepository;
    private ContaTransfer contaTransfer;

    @BeforeEach
    void setUp() {
        contaRepository = mock(ContaRepository.class);
        contaTransfer = new ContaTransfer();
        contaTransfer.contaRepository = contaRepository;
    }

    @Test
    void testTransferirComSucesso() {
        // Arrange - Criação dos DTOs e objetos de conta
        ModeloTransferDTO dto = new ModeloTransferDTO();
        dto.setNumeroContaOrigem(1);
        dto.setNumeroContaDestino(2);
        dto.setValorTransferencia(100.0);
        dto.setCpfOrigem("123");

        // Conta de origem com saldo inicial
        ModelConta contaOrigem = new ModelConta();
        contaOrigem.setNumeroConta(1);
        contaOrigem.setSaldo(200.0); // Saldo inicial para a conta de origem
        contaOrigem.setCpf("123");
        
        // Conta de destino com saldo inicial
        ModelConta contaDestino = new ModelConta();
        contaDestino.setNumeroConta(2);
        contaDestino.setSaldo(50.0); // Saldo inicial para a conta de destino

        // Mock do comportamento do repositório
        when(contaRepository.findByNumeroContaUpdate(1)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByNumeroContaUpdate(2)).thenReturn(Optional.of(contaDestino));
        when(contaRepository.save(any(ModelConta.class))).thenAnswer(i -> i.getArgument(0)); // Simula o save

        // Act - Chamando o método de transferência
        contaTransfer.transferir(dto);

        // Assert - Verificação se o saldo foi atualizado corretamente
        assertEquals(100.0, contaOrigem.getSaldo()); // O saldo de origem deve ser 200 - 100 = 100
        assertEquals(150.0, contaDestino.getSaldo()); // O saldo de destino deve ser 50 + 100 = 150

        // Verifica se o repositório foi chamado corretamente
        verify(contaRepository).save(contaOrigem);
        verify(contaRepository).save(contaDestino);
    }

    @Test
    void testTransferirValorZero() {
        ModeloTransferDTO dto = new ModeloTransferDTO();
        dto.setValorTransferencia(0.0);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> contaTransfer.transferir(dto));
        assertEquals("Valor de transferência deve ser maior que zero!", ex.getMessage());
    }

    @Test
    void testTransferirContasIguais() {
        ModeloTransferDTO dto = new ModeloTransferDTO();
        dto.setNumeroContaOrigem(1);
        dto.setNumeroContaDestino(1);
        dto.setValorTransferencia(10.0);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> contaTransfer.transferir(dto));
        assertEquals("A conta de origem e destino não podem ser iguais!", ex.getMessage());
    }

    @Test
    void testContaOrigemNaoExiste() {
        ModeloTransferDTO dto = new ModeloTransferDTO();
        dto.setNumeroContaOrigem(1);
        dto.setNumeroContaDestino(2);
        dto.setValorTransferencia(10.0);

        when(contaRepository.findByNumeroContaUpdate(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> contaTransfer.transferir(dto));
        assertEquals("Conta de origem não encontrada!", ex.getMessage());
    }

    @Test
    void testContaDestinoNaoExiste() {
        ModeloTransferDTO dto = new ModeloTransferDTO();
        dto.setNumeroContaOrigem(1);
        dto.setNumeroContaDestino(2);
        dto.setValorTransferencia(10.0);
        dto.setCpfOrigem("123");

        ModelConta contaOrigem = new ModelConta();
        contaOrigem.setNumeroConta(1);
        contaOrigem.setCpf("123");
        contaOrigem.setSaldo(100.0);

        when(contaRepository.findByNumeroContaUpdate(1)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByNumeroContaUpdate(2)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> contaTransfer.transferir(dto));
        assertEquals("Conta de destino não encontrada!", ex.getMessage());
    }

    @Test
    void testCpfOrigemInvalido() {
        ModeloTransferDTO dto = new ModeloTransferDTO();
        dto.setNumeroContaOrigem(1);
        dto.setNumeroContaDestino(2);
        dto.setValorTransferencia(10.0);
        dto.setCpfOrigem("999");

        ModelConta contaOrigem = new ModelConta();
        contaOrigem.setNumeroConta(1);
        contaOrigem.setCpf("123");
        contaOrigem.setSaldo(100.0);

        when(contaRepository.findByNumeroContaUpdate(1)).thenReturn(Optional.of(contaOrigem));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> contaTransfer.transferir(dto));
        assertEquals("O CPF da conta de origem não confere!", ex.getMessage());
    }

    @Test
    void testSaldoInsuficiente() {
        ModeloTransferDTO dto = new ModeloTransferDTO();
        dto.setNumeroContaOrigem(1);
        dto.setNumeroContaDestino(2);
        dto.setValorTransferencia(200.0);
        dto.setCpfOrigem("123");

        ModelConta contaOrigem = new ModelConta();
        contaOrigem.setNumeroConta(1);
        contaOrigem.setCpf("123");
        contaOrigem.setSaldo(100.0);

        ModelConta contaDestino = new ModelConta();
        contaDestino.setNumeroConta(2);
        contaDestino.setCpf("456");
        contaDestino.setSaldo(50.0);

        when(contaRepository.findByNumeroContaUpdate(1)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findByNumeroContaUpdate(2)).thenReturn(Optional.of(contaDestino));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> contaTransfer.transferir(dto));
        assertEquals("Saldo insuficiente na conta de origem!", ex.getMessage());
    }
}
