package com.biit.kafka.core.consumers;

import com.biit.kafka.config.KafkaConfig;
import com.biit.kafka.consumers.HistoricalEventConsumer;
import com.biit.kafka.core.models.StringEvent;
import org.springframework.stereotype.Component;

@Component
public class HistoricalStringEventConsumer extends HistoricalEventConsumer<StringEvent> {

    public HistoricalStringEventConsumer(KafkaConfig kafkaConfig) {
        super(StringEvent.class, kafkaConfig);
    }

}
