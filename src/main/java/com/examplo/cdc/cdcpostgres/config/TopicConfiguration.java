package com.examplo.cdc.cdcpostgres.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;

@Configuration
class TopicConfiguration {

    private final KafkaProperties kafkaProperties;
    private final String topico;

    TopicConfiguration(KafkaProperties kafkaProperties,
                       @Value("${topic.name}") String topico) {

        this.kafkaProperties = kafkaProperties;
        this.topico = topico;
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name(topico)
            .partitions(1)
            .replicas(1)
            .build();
    }

}
