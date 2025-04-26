package com.teste.banco.banco.Services;

import com.teste.banco.banco.Models.ModelConta;
import com.teste.banco.banco.Repository.ContaRepository;
import com.teste.banco.banco.exceptions.ContaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContaMovimentacaoTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaMovimentacao contaMovimentacao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void adicionarCredito_DeveCreditarComSucesso() {
        ModelConta conta = new ModelConta();
        conta.setNumeroConta(1);
        conta.setSaldo(100.0);

        ModelConta contaExistente = new ModelConta();
        contaExistente.setNumeroConta(1);
        contaExistente.setSaldo(50.0);

        when(contaRepository.findByNumeroContaUpdate(1)).thenReturn(Optional.of(contaExistente));
        when(contaRepository.save(any(ModelConta.class))).thenAnswer(i -> i.getArgument(0));

        ModelConta result = contaMovimentacao.AdicionarCredito(conta);

        assertEquals(150.0, result.getSaldo());
        verify(contaRepository).save(contaExistente);
    }

    @Test
    void adicionarCredito_DeveLancarExcecao_ValorNegativo() {
        ModelConta conta = new ModelConta();
        conta.setNumeroConta(1);
        conta.setSaldo(-10.0);

        Exception ex = assertThrows(ContaException.class, () -> contaMovimentacao.AdicionarCredito(conta));
        assertEquals("Valor de crédito não pode ser negativo!", ex.getMessage());
    }

    @Test
    void adicionarCredito_DeveLancarExcecao_ContaNaoEncontrada() {
        ModelConta conta = new ModelConta();
        conta.setNumeroConta(1);
        conta.setSaldo(10.0);

        when(contaRepository.findByNumeroContaUpdate(1)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ContaException.class, () -> contaMovimentacao.AdicionarCredito(conta));
        assertEquals("Conta não encontrada!", ex.getMessage());
    }

    @Test
    void debitarCredito_DeveDebitarComSucesso() {
        ModelConta conta = new ModelConta();
        conta.setNumeroConta(1);
        conta.setSaldo(30.0);

        ModelConta contaExistente = new ModelConta();
        contaExistente.setNumeroConta(1);
        contaExistente.setSaldo(100.0);

        when(contaRepository.findByNumeroContaUpdate(1)).thenReturn(Optional.of(contaExistente));
        when(contaRepository.save(any(ModelConta.class))).thenAnswer(i -> i.getArgument(0));

        ModelConta result = contaMovimentacao.DebitarCredito(conta);

        assertEquals(70.0, result.getSaldo());
        verify(contaRepository).save(contaExistente);
    }

    @Test
    void debitarCredito_DeveLancarExcecao_ValorNegativo() {
        ModelConta conta = new ModelConta();
        conta.setNumeroConta(1);
        conta.setSaldo(-10.0);

        Exception ex = assertThrows(ContaException.class, () -> contaMovimentacao.DebitarCredito(conta));
        assertEquals("Valor de crédito não pode ser negativo!", ex.getMessage());
    }

    @Test
    void debitarCredito_DeveLancarExcecao_SaldoInsuficiente() {
        ModelConta conta = new ModelConta();
        conta.setNumeroConta(1);
        conta.setSaldo(200.0);

        ModelConta contaExistente = new ModelConta();
        contaExistente.setNumeroConta(1);
        contaExistente.setSaldo(100.0);

        when(contaRepository.findByNumeroContaUpdate(1)).thenReturn(Optional.of(contaExistente));

        Exception ex = assertThrows(ContaException.class, () -> contaMovimentacao.DebitarCredito(conta));
        assertEquals("Saldo insuficiente!", ex.getMessage());
    }

    @Test
    void debitarCredito_DeveLancarExcecao_ContaNaoEncontrada() {
        ModelConta conta = new ModelConta();
        conta.setNumeroConta(100);
        conta.setSaldo(10.0);

        when(contaRepository.findByNumeroContaUpdate(100)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ContaException.class, () -> contaMovimentacao.DebitarCredito(conta));
        assertEquals("Conta não encontrada!", ex.getMessage());
    }
}
