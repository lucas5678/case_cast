package com.teste.banco.banco.Repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teste.banco.banco.Models.ModelUsuarios;

public interface UsuariosRepository extends JpaRepository<ModelUsuarios, Long> {
    
    Optional<ModelUsuarios> findByCpfAndSenha(String cpf, String senha);

    Optional<ModelUsuarios> findByCpf(String cpf);
}
