package com.biit.kafka.core.controllers;

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

import com.biit.kafka.controllers.models.EventDTO;
import com.biit.kafka.converters.models.EventConverterRequest;
import com.biit.kafka.core.converters.ElementEventConverter;
import com.biit.kafka.core.providers.EventProvider;
import com.biit.kafka.events.Event;
import com.biit.server.controller.SimpleController;
import com.biit.server.exceptions.ValidateBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Controller
public class EventController extends SimpleController<Event, EventDTO,
        EventProvider, EventConverterRequest, ElementEventConverter> {

    @Autowired
    protected EventController(EventProvider provider, ElementEventConverter converter) {
        super(provider, converter);
    }

    @Override
    protected EventConverterRequest createConverterRequest(Event event) {
        return new EventConverterRequest(event);
    }

    @Override
    public EventDTO create(EventDTO eventDTO, String creatorName) {
        populateEvent(eventDTO, creatorName);
        validate(eventDTO);
        getProvider().send(reverse(eventDTO));
        return eventDTO;
    }

    private void populateEvent(EventDTO eventDTO, String creatorName) {
        eventDTO.setCreatedBy(creatorName);
    }

    @Override
    public Collection<EventDTO> create(Collection<EventDTO> eventDTOS, String creatorName) {
        eventDTOS.forEach(eventDTO -> populateEvent(eventDTO, creatorName));
        validate(eventDTOS);
        getProvider().sendAll(reverseAll(eventDTOS));
        return eventDTOS;
    }


    public EventDTO create(String topic, String key, Integer partition, Long timestamp, EventDTO eventDTO, String creatorName) {
        populateEvent(eventDTO, creatorName);
        validate(eventDTO);
        getProvider().send(topic, key, partition, timestamp, reverse(eventDTO));
        return eventDTO;
    }


    public Collection<EventDTO> create(String topic, String key, Integer partition, long timestamp, Collection<EventDTO> eventDTOS,
                                       String creatorName) {
        eventDTOS.forEach(eventDTO -> populateEvent(eventDTO, creatorName));
        validate(eventDTOS);
        getProvider().sendAll(topic, key, partition, timestamp, reverseAll(eventDTOS));
        return eventDTOS;
    }

    public Collection<EventDTO> get(Collection<String> topics, LocalDateTime startingTime, Duration duration) {
        return convertAll(getProvider().get(topics, startingTime, duration));
    }

    @Override
    public void validate(EventDTO eventDTO) throws ValidateBadRequestException {
        if (eventDTO.getMessageId() == null) {
            eventDTO.setMessageId(UUID.randomUUID());
        }
        if (eventDTO.getCreatedAt() == null) {
            eventDTO.setCreatedAt(LocalDateTime.now());
        }
    }
}
