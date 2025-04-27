package com.teste.banco.banco.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.teste.banco.banco.DTO.ModelContaDTO;
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
        // Arrange
        ModelContaDTO contaDTO = new ModelContaDTO();
        contaDTO.setCpf("419442078050000");
        contaDTO.setNumeroConta(12345);
        contaDTO.setSaldo(100.0);
        contaDTO.setTitular("João");

        ModelConta contaEntity = contaDTO.toEntity();

        // Mock do comportamento do repository
        when(contaRepository.existsByCpf(contaDTO.getCpf())).thenReturn(false);
        when(contaRepository.existsByNumeroConta(contaDTO.getNumeroConta())).thenReturn(false);
        when(contaRepository.save(any(ModelConta.class))).thenReturn(contaEntity);

        // Act
        ModelConta result = contaService.salvarConta(contaDTO);

        // Assert — valida campo a campo
        assertEquals(contaDTO.getCpf(), result.getCpf());
        assertEquals(contaDTO.getNumeroConta(), result.getNumeroConta());
        assertEquals(contaDTO.getSaldo(), result.getSaldo());
        assertEquals(contaDTO.getTitular(), result.getTitular());

        // Verifica se o método save foi chamado com qualquer instância de ModelConta
        verify(contaRepository).save(any(ModelConta.class));
    }

    @Test
    void salvarConta_DeveLancarExcecao_SeCpfJaCadastrado() {
        ModelContaDTO conta = new ModelContaDTO();
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
        ModelContaDTO conta = new ModelContaDTO();
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
        ModelContaDTO conta = new ModelContaDTO();
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
