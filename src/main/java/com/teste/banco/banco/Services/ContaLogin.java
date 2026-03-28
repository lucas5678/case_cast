package com.teste.banco.banco.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.teste.banco.banco.DTO.ModelLoginDTO;
import com.teste.banco.banco.Models.ModelConta;
import com.teste.banco.banco.Models.ModelUsuarios;
import com.teste.banco.banco.Repository.ContaRepository;
import com.teste.banco.banco.Repository.UsuariosRepository;

@Service
public class ContaLogin {
    private static final Logger log = LoggerFactory.getLogger(ContaLogin.class);
    
    @Autowired
    UsuariosRepository usuariosRepository;

    @Autowired
    ContaRepository contaRepository;

    public ModelLoginDTO logar(ModelLoginDTO usuario) {
        log.info("Tentativa de login para CPF: {}", usuario.getCpf());
        
        try {
            ModelLoginDTO usuarioBanco = new ModelLoginDTO();
            // verifica se usuarios e senha existem no banco de dados
            ModelUsuarios usuarioBancoEntity = usuariosRepository.findByCpfAndSenha(usuario.getCpf(), usuario.getSenha())
                    .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos!"));
            
            log.info("Login bem-sucedido para CPF: {} | Perfil: {}", usuarioBancoEntity.getCpf(), usuarioBancoEntity.getPerfil());
            
            if ("user".equals(usuarioBancoEntity.getPerfil())) {
                if (!usuarioBancoEntity.getCpf().equals("")) {
                    ModelConta contaBancoEntity = contaRepository.findByCpf(usuarioBancoEntity.getCpf())
                            .orElseThrow(() -> new RuntimeException("Conta não encontrada!"));
                    usuarioBanco.setNumeroConta(contaBancoEntity.getNumeroConta());
                    usuarioBanco.setTitular(usuarioBancoEntity.getNome());
                    usuarioBanco.setSaldo(contaBancoEntity.getSaldo());
                }
            }
            usuarioBanco.setCpf(usuarioBancoEntity.getCpf());
            usuarioBanco.setSenha(usuarioBancoEntity.getSenha());
            usuarioBanco.setPerfil(usuarioBancoEntity.getPerfil());
            usuarioBanco.setNome(usuarioBancoEntity.getNome());
            return usuarioBanco;
        } catch (RuntimeException e) {
            log.warn("Falha no login para CPF: {} | Motivo: {}", usuario.getCpf(), e.getMessage());
            throw e;
        }
    }

    public ModelLoginDTO AtualizaInformacoes(ModelLoginDTO usuario) {
        ModelLoginDTO usuarioBanco = new ModelLoginDTO();
        ModelUsuarios usuarioBancoEntity = usuariosRepository.findByCpf(usuario.getCpf())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos!"));
        // verifica se usuario é admin ou user
        if ("user".equals(usuarioBancoEntity.getPerfil())) {
            if (!usuarioBancoEntity.getCpf().equals("")) {
                ModelConta contaBancoEntity = contaRepository.findByCpf(usuarioBancoEntity.getCpf())
                        .orElseThrow(() -> new RuntimeException("Conta não encontrada!"));
                usuarioBanco.setNumeroConta(contaBancoEntity.getNumeroConta());
                usuarioBanco.setTitular(usuarioBancoEntity.getNome());
                usuarioBanco.setSaldo(contaBancoEntity.getSaldo());
            }
        }

        usuarioBanco.setCpf(usuarioBancoEntity.getCpf());
        usuarioBanco.setSenha(usuarioBancoEntity.getSenha());
        usuarioBanco.setPerfil(usuarioBancoEntity.getPerfil());
        usuarioBanco.setNome(usuarioBancoEntity.getNome());
        return usuarioBanco;
    }
}