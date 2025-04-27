package com.teste.banco.banco.Services;

import com.teste.banco.banco.DTO.ModelLoginDTO;
import com.teste.banco.banco.Models.ModelUsuarios;
import com.teste.banco.banco.Repository.UsuariosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContaLoginTest {

    public UsuariosRepository usuariosRepository;
    private ContaLogin contaLogin;

    @BeforeEach
    void setUp() {
        usuariosRepository = mock(UsuariosRepository.class);
        contaLogin = new ContaLogin();
        // Injetando o mock manualmente
        contaLogin.usuariosRepository = usuariosRepository;
    }

    @Test
    void testLogarComSucesso() {
        ModelLoginDTO dto = new ModelLoginDTO();
        dto.setCpf("123");
        dto.setSenha("senha");

        ModelUsuarios usuario = new ModelUsuarios();
        usuario.setCpf("123");
        usuario.setSenha("senha");

        when(usuariosRepository.findByCpfAndSenha("123", "senha")).thenReturn(Optional.of(usuario));

        ModelUsuarios result = contaLogin.logar(dto);

        assertNotNull(result);
        assertEquals("123", result.getCpf());
        assertEquals("senha", result.getSenha());
    }

    @Test
    void testLogarComFalha() {
        ModelLoginDTO dto = new ModelLoginDTO();
        dto.setCpf("123");
        dto.setSenha("errada");

        when(usuariosRepository.findByCpfAndSenha("123", "errada")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> contaLogin.logar(dto));
        assertEquals("Usuário ou senha inválidos!", ex.getMessage());
    }
}
