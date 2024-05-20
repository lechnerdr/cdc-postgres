package com.examplo.cdc.cdcpostgres.repository;

import com.examplo.cdc.cdcpostgres.entity.Teste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesteRepository extends JpaRepository<Teste, Long> {
}
