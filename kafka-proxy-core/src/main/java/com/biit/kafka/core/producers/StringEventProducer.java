package com.biit.kafka.core.producers;

import com.biit.kafka.config.KafkaConfig;
import com.biit.kafka.core.models.StringEvent;
import com.biit.kafka.producers.TemplateEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StringEventProducer extends TemplateEventProducer<StringEvent> {

    @Autowired
    public StringEventProducer(KafkaConfig kafkaConfig) {
        super(kafkaConfig);
    }

}
