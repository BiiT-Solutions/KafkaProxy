package com.biit.kafka.core.providers;

import com.biit.kafka.core.models.StringEvent;
import com.biit.kafka.events.KafkaEventTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EventProvider {

    private final KafkaEventTemplate<String, StringEvent> kafkaTemplate;

    public EventProvider(KafkaEventTemplate<String, StringEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void save(StringEvent event) {
        kafkaTemplate.send(event);
    }

    public void saveAll(Collection<StringEvent> events) {
        events.forEach(this::save);
    }
}
