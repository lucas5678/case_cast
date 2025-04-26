package com.teste.banco.banco.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teste.banco.banco.Models.ModelConta;

import jakarta.persistence.LockModeType;

public interface ContaRepository extends JpaRepository<ModelConta, Long> {

    List<ModelConta> findByTitular(String titular);

    Optional<ModelConta> findByCpf(String cpf);

    Optional<ModelConta> findByNumeroConta(int numeroConta);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM ModelConta c WHERE c.numeroConta = :numeroConta")
    Optional<ModelConta> findByNumeroContaUpdate(@Param("numeroConta") int numeroConta);

    boolean existsByCpf(String numeroConta);

    boolean existsByNumeroConta(int numeroConta);
}
