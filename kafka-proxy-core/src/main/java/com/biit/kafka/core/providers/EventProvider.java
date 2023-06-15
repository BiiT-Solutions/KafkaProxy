package com.biit.kafka.core.providers;

import com.biit.kafka.core.consumers.HistoricalStringEventConsumer;
import com.biit.kafka.core.models.StringEvent;
import com.biit.kafka.events.KafkaEventTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class EventProvider {

    private final KafkaEventTemplate<String, StringEvent> kafkaTemplate;

    private final HistoricalStringEventConsumer historicalStringEventConsumer;

    public EventProvider(KafkaEventTemplate<String, StringEvent> kafkaTemplate, HistoricalStringEventConsumer historicalStringEventConsumer) {
        this.kafkaTemplate = kafkaTemplate;
        this.historicalStringEventConsumer = historicalStringEventConsumer;
    }

    /**
     * Sends and event to the default kafka topic defined in the 'application.properties' file.
     *
     * @param event the event to send.
     */
    public void send(StringEvent event) {
        kafkaTemplate.send(event);
    }

    /**
     * Sends and event to a specific topic.
     *
     * @param event the event to send.
     */
    public void send(String topic, StringEvent event) {
        if (topic == null || topic.isBlank()) {
            send(event);
        } else {
            kafkaTemplate.send(topic, event);
        }
    }

    public void sendAll(Collection<StringEvent> events) {
        events.forEach(this::send);
    }

    public void sendAll(String topic, Collection<StringEvent> events) {
        if (topic == null || topic.isBlank()) {
            sendAll(events);
        } else {
            events.forEach(event -> send(topic, event));
        }
    }

    public Collection<StringEvent> get(Collection<String> topics, LocalDateTime startingTime, Duration duration) {
        if (topics == null || topics.isEmpty()) {
            return historicalStringEventConsumer.getEvents(startingTime, duration);
        } else {
            return historicalStringEventConsumer.getEvents(topics, startingTime, duration);
        }
    }
}
