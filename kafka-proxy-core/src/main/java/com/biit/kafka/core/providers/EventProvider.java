package com.biit.kafka.core.providers;

/*-
 * #%L
 * Kafka Proxy (Core)
 * %%
 * Copyright (C) 2023 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.kafka.consumers.HistoricalEventConsumer;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.KafkaEventTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class EventProvider {

    private final KafkaEventTemplate kafkaTemplate;

    private final HistoricalEventConsumer historicalEventConsumer;

    public EventProvider(@Autowired(required = false) KafkaEventTemplate kafkaTemplate,
                         @Autowired(required = false) HistoricalEventConsumer historicalEventConsumer) {
        this.kafkaTemplate = kafkaTemplate;
        this.historicalEventConsumer = historicalEventConsumer;
    }

    /**
     * Sends and event to the default kafka topic defined in the 'application.properties' file.
     *
     * @param event the event to send.
     */
    public void send(Event event) {
        if (kafkaTemplate != null) {
            kafkaTemplate.send(event);
        }
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
            if (kafkaTemplate != null) {
                kafkaTemplate.send(topic, event);
            }
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
    public void send(String topic, String key, Integer partition, Long timestamp, Event event) {
        if (kafkaTemplate != null) {
            kafkaTemplate.send(topic, key, partition, timestamp, event);
        }
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
        if (historicalEventConsumer != null) {
            if (topics == null || topics.isEmpty()) {
                return historicalEventConsumer.getEvents(startingTime, duration);
            } else {
                return historicalEventConsumer.getEvents(topics, startingTime, duration);
            }
        }
        return new ArrayList<>();
    }
}
