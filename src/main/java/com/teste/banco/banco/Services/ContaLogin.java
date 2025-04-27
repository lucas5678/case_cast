package com.teste.banco.banco.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.teste.banco.banco.DTO.ModelLoginDTO;
import com.teste.banco.banco.Models.ModelUsuarios;
import com.teste.banco.banco.Repository.UsuariosRepository;

@Service
public class ContaLogin {
    @Autowired
    UsuariosRepository usuariosRepository;

    public ModelUsuarios logar(ModelLoginDTO usuario) {

        // verifica se usuarios e senha existem no banco de dados
        ModelUsuarios usuarioBanco = usuariosRepository.findByCpfAndSenha(usuario.getCpf(), usuario.getSenha())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos!"));
        return usuarioBanco;

    }

}
