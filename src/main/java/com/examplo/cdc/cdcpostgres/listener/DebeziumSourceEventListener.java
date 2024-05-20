package com.examplo.cdc.cdcpostgres.listener;

import com.examplo.cdc.cdcpostgres.service.NotificarCriacaoTesteService;
import io.debezium.config.Configuration;
import io.debezium.data.Envelope;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static io.debezium.data.Envelope.FieldName.*;
import static java.util.stream.Collectors.toMap;

@Component
@Slf4j
class DebeziumSourceEventListener {

    private final Executor executor;
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
    private final NotificarCriacaoTesteService notificarCriacaoTesteService;


    DebeziumSourceEventListener(Configuration postgresDbConnector,
                                NotificarCriacaoTesteService notificarCriacaoTesteService) {

        this.executor = Executors.newSingleThreadExecutor();

        this.debeziumEngine =
            DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(postgresDbConnector.asProperties())
                .notifying(this::handleChangeEvent)
                .build();

        this.notificarCriacaoTesteService = notificarCriacaoTesteService;

    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {

        var sourceRecord = sourceRecordRecordChangeEvent.record();
        log.info("Key = {}, Value = {}", sourceRecord.key(), sourceRecord.value());

        var sourceRecordChangeValue = (Struct) sourceRecord.value();
        log.info("SourceRecordChangeValue = '{}'", sourceRecordChangeValue);

        if (sourceRecordChangeValue != null) {

            var operation = Envelope.Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));

            if (operation != Envelope.Operation.DELETE) {

                var struct = (Struct) sourceRecordChangeValue.get(AFTER);

                Map<String, Object> payload = struct.schema().fields().stream()
                    .map(Field::name)
                    .filter(fieldName -> struct.get(fieldName) != null)
                    .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
                    .collect(toMap(Pair::getKey, Pair::getValue));

                var nome = (String) payload.get("name");
                var email = (String) payload.get("email");
                var traceId = (String) payload.get("trace_id");

                this.notificarCriacaoTesteService.notificarCriacaoTeste(nome, email, traceId);
                log.info("Updated Data: {} with Operation: {}", payload, operation.name());
            }
        }

    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }

}
