package com.biit.kafka.core.controllers;

import com.biit.kafka.core.converters.EventConverter;
import com.biit.kafka.core.converters.models.EventConverterRequest;
import com.biit.kafka.core.models.EventDTO;
import com.biit.kafka.core.models.StringEvent;
import com.biit.kafka.core.providers.EventProvider;
import com.biit.server.controller.SimpleController;
import com.biit.server.exceptions.ValidateBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Controller
public class EventController extends SimpleController<StringEvent, EventDTO,
        EventProvider, EventConverterRequest, EventConverter> {

    @Autowired
    protected EventController(EventProvider provider, EventConverter converter) {
        super(provider, converter);
    }

    @Override
    protected EventConverterRequest createConverterRequest(StringEvent event) {
        return new EventConverterRequest(event);
    }

    @Override
    public EventDTO create(EventDTO eventDTO, String creatorName) {
        eventDTO.setCreatedBy(creatorName);
        validate(eventDTO);
        getProvider().send(reverse(eventDTO));
        return eventDTO;
    }

    @Override
    public Collection<EventDTO> create(Collection<EventDTO> eventDTOS, String creatorName) {
        eventDTOS.forEach(eventDTO -> eventDTO.setCreatedBy(creatorName));
        validate(eventDTOS);
        getProvider().sendAll(reverseAll(eventDTOS));
        return eventDTOS;
    }

    public EventDTO create(EventDTO eventDTO, String topic, String creatorName) {
        eventDTO.setCreatedBy(creatorName);
        getProvider().send(topic, reverse(eventDTO));
        return eventDTO;
    }

    public Collection<EventDTO> create(Collection<EventDTO> eventDTOS, String topic, String creatorName) {
        eventDTOS.forEach(eventDTO -> eventDTO.setCreatedBy(creatorName));
        getProvider().sendAll(topic, reverseAll(eventDTOS));
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
    }
}
