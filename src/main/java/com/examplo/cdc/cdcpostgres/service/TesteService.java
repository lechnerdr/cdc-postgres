package com.examplo.cdc.cdcpostgres.service;

import com.examplo.cdc.cdcpostgres.controller.request.TesteRequest;
import com.examplo.cdc.cdcpostgres.entity.Teste;
import com.examplo.cdc.cdcpostgres.entity.TesteOutbox;
import com.examplo.cdc.cdcpostgres.repository.TesteOutboxRepository;
import com.examplo.cdc.cdcpostgres.repository.TesteRepository;
import io.micrometer.tracing.Tracer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TesteService {

    private final Tracer tracer;
    private final TesteRepository testeRepository;
    private final TesteOutboxRepository testeOutboxRepository;

    @Transactional
    public Teste salvarTeste(TesteRequest testeRequest) {

        var traceId = tracer.nextSpan().context().traceId();

        var testeEntity = new Teste();
        testeEntity.setName(testeRequest.name());
        testeEntity.setEmail(testeRequest.email());

        var testeEntitySalvo = testeRepository.save(testeEntity);

        var testeOutbox = new TesteOutbox();
        testeOutbox.setTraceId(traceId);
        testeOutbox.setName(testeEntitySalvo.getName());
        testeOutbox.setEmail(testeEntitySalvo.getEmail());

        testeOutboxRepository.save(testeOutbox);

        return testeEntitySalvo;
    }

}
