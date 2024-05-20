package com.examplo.cdc.cdcpostgres.repository;

import com.examplo.cdc.cdcpostgres.entity.TesteOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesteOutboxRepository extends JpaRepository<TesteOutbox, Long> {
}
