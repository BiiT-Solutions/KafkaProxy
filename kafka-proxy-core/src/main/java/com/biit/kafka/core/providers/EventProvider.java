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

    private final KafkaEventTemplate kafkaTemplate;

    private final HistoricalStringEventConsumer historicalStringEventConsumer;

    public EventProvider(KafkaEventTemplate kafkaTemplate, HistoricalStringEventConsumer historicalStringEventConsumer) {
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

    /**
     * Sends and event.
     *
     * @param topic     the topic to use. Optional and if not set, will be use the one set on 'application.properties'.
     * @param key       the key of the message.
     * @param partition the partition of the message. Can be null, but if set, requires a key with a valid value.
     * @param timestamp the timestamp of the message. Can be null, but if set, requires a partition with a valid value.
     * @param event     the event to send.
     */
    public void send(String topic, String key, Integer partition, long timestamp, StringEvent event) {
        kafkaTemplate.send(topic, key, partition, timestamp, event);
    }

    public void sendAll(Collection<StringEvent> events) {
        events.forEach(this::send);
    }

    /**
     * Sends a bunch of events.
     *
     * @param topic     the topic to use. Optional and if not set, will be use the one set on 'application.properties'.
     * @param key       the key of the message.
     * @param partition the partition of the message. Can be null, but if set, requires a key with a valid value.
     * @param timestamp the timestamp of the message. Can be null, but if set, requires a partition with a valid value.
     * @param events    the collection of events to sent.
     */
    public void sendAll(String topic, String key, Integer partition, long timestamp, Collection<StringEvent> events) {
        events.forEach(event -> send(topic, key, partition, timestamp, event));
    }

    public Collection<StringEvent> get(Collection<String> topics, LocalDateTime startingTime, Duration duration) {
        if (topics == null || topics.isEmpty()) {
            return historicalStringEventConsumer.getEvents(startingTime, duration);
        } else {
            return historicalStringEventConsumer.getEvents(topics, startingTime, duration);
        }
    }
}
