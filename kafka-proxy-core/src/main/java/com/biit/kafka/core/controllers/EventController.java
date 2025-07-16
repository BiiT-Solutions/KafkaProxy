package com.biit.kafka.core.controllers;

import com.biit.kafka.controllers.models.EventDTO;
import com.biit.kafka.converters.models.EventConverterRequest;
import com.biit.kafka.core.converters.ElementEventConverter;
import com.biit.kafka.core.providers.EventProvider;
import com.biit.kafka.events.Event;
import com.biit.server.controller.SimpleController;
import com.biit.server.exceptions.ValidateBadRequestException;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Controller
public class EventController extends SimpleController<Event, EventDTO,
        EventProvider, EventConverterRequest, ElementEventConverter> {

    private final List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProviders;

    @Autowired
    protected EventController(EventProvider provider, ElementEventConverter converter,
                              List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProviders) {
        super(provider, converter);
        this.userOrganizationProviders = userOrganizationProviders;
    }

    @Override
    protected EventConverterRequest createConverterRequest(Event event) {
        return new EventConverterRequest(event);
    }

    @Override
    public EventDTO create(EventDTO eventDTO, String creatorName) {
        eventDTO.setCreatedBy(creatorName);
        validate(eventDTO);
        getProvider().send(reverse(eventDTO));
        return eventDTO;
    }

    private void populateEvent(EventDTO eventDTO, String creatorName) {
        eventDTO.setCreatedBy(creatorName);
        if (eventDTO.getOrganization() == null && !userOrganizationProviders.isEmpty()) {
            final Collection<? extends IUserOrganization> organizations = userOrganizationProviders.get(0).findByUsername(creatorName);
            if (!organizations.isEmpty()) {
                eventDTO.setOrganization(organizations.iterator().next().getName());
            }
        }
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
        eventDTOS.forEach(eventDTO -> eventDTO.setCreatedBy(creatorName));
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
