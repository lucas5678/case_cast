package com.teste.banco.banco.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.teste.banco.banco.Models.ModelConta;
import com.teste.banco.banco.Repository.ContaRepository;
import com.teste.banco.banco.exceptions.ContaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ContaServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ContaServiceTest.class);

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void salvarConta_DeveSalvarComSucesso() {
        ModelConta conta = new ModelConta();
        conta.setCpf("12345678900");
        conta.setNumeroConta(12345);
        conta.setSaldo(100.0);

        when(contaRepository.existsByCpf(conta.getCpf())).thenReturn(false);
        when(contaRepository.existsByNumeroConta(conta.getNumeroConta())).thenReturn(false);
        when(contaRepository.save(conta)).thenReturn(conta);

        ModelConta result = contaService.salvarConta(conta);

        assertEquals(conta, result);
        verify(contaRepository).save(conta);
    }

    @Test
    void salvarConta_DeveLancarExcecao_SeCpfJaCadastrado() {
        ModelConta conta = new ModelConta();
        conta.setCpf("419442078050");
        conta.setNumeroConta(12345);
        conta.setSaldo(100.0);

        when(contaRepository.existsByCpf(conta.getCpf())).thenReturn(true);

        ContaException ex = assertThrows(ContaException.class, () -> contaService.salvarConta(conta));
        logger.info("Exceção lançada: {}", ex.getMessage());
        assertEquals("CPF já cadastrado!", ex.getMessage());
        verify(contaRepository, never()).save(any());
    }

    @Test
    void salvarConta_DeveLancarExcecao_SeNumeroContaJaExiste() {
        ModelConta conta = new ModelConta();
        conta.setCpf("41944207805");
        conta.setNumeroConta(12345);
        conta.setSaldo(100.0);

        when(contaRepository.existsByCpf("12345678900")).thenReturn(false);
        when(contaRepository.existsByNumeroConta(12345)).thenReturn(true);

        ContaException ex = assertThrows(ContaException.class, () -> contaService.salvarConta(conta));
        assertEquals("Número da conta já existe!", ex.getMessage());
        verify(contaRepository, never()).save(any());
    }

    @Test
    void salvarConta_DeveLancarExcecao_SeSaldoNegativo() {
        ModelConta conta = new ModelConta();
        conta.setCpf("419442078051111");
        conta.setNumeroConta(12345);
        conta.setSaldo(-50.0);

        when(contaRepository.existsByCpf("12345678900")).thenReturn(false);
        when(contaRepository.existsByNumeroConta(12345)).thenReturn(false);

        ContaException ex = assertThrows(ContaException.class, () -> contaService.salvarConta(conta));
        assertEquals("Saldo não pode ser negativo!", ex.getMessage());
        verify(contaRepository, never()).save(any());
    }
}
