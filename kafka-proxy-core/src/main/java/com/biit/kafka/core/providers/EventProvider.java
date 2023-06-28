package com.biit.kafka.core.providers;

import com.biit.kafka.consumers.HistoricalEventConsumer;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.KafkaEventTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class EventProvider {

    private final KafkaEventTemplate kafkaTemplate;

    private final HistoricalEventConsumer historicalEventConsumer;

    public EventProvider(KafkaEventTemplate kafkaTemplate, HistoricalEventConsumer historicalEventConsumer) {
        this.kafkaTemplate = kafkaTemplate;
        this.historicalEventConsumer = historicalEventConsumer;
    }

    /**
     * Sends and event to the default kafka topic defined in the 'application.properties' file.
     *
     * @param event the event to send.
     */
    public void send(Event event) {
        kafkaTemplate.send(event);
    }

    /**
     * Sends and event to a specific topic.
     *
     * @param event the event to send.
     */
    public void send(String topic, Event event) {
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
    public void send(String topic, String key, Integer partition, long timestamp, Event event) {
        kafkaTemplate.send(topic, key, partition, timestamp, event);
    }

    public void sendAll(Collection<Event> events) {
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
    public void sendAll(String topic, String key, Integer partition, long timestamp, Collection<Event> events) {
        events.forEach(event -> send(topic, key, partition, timestamp, event));
    }

    public Collection<Event> get(Collection<String> topics, LocalDateTime startingTime, Duration duration) {
        if (topics == null || topics.isEmpty()) {
            return historicalEventConsumer.getEvents(startingTime, duration);
        } else {
            return historicalEventConsumer.getEvents(topics, startingTime, duration);
        }
    }
}
