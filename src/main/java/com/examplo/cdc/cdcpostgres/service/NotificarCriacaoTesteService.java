package com.examplo.cdc.cdcpostgres.service;

import com.examplo.cdc.cdcpostgres.domain.TesteMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificarCriacaoTesteService {

    private final String topico;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    NotificarCriacaoTesteService(@Value("${topic.name}") String topico,
                                        KafkaTemplate<String, Object> kafkaTemplate) {

        this.topico = topico;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void notificarCriacaoTeste(String nome, String email, String traceId) {
        var testeMessage = new TesteMessage(nome, email, traceId);

        kafkaTemplate.send(topico, testeMessage);

        log.info("Teste notificado com sucesso: {}", testeMessage);
    }

}
