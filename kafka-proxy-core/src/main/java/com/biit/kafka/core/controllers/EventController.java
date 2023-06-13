package com.biit.kafka.core.controllers;

import com.biit.kafka.core.converters.EventConverter;
import com.biit.kafka.core.converters.models.EventConverterRequest;
import com.biit.kafka.core.models.EventDTO;
import com.biit.kafka.core.models.StringEvent;
import com.biit.kafka.core.providers.EventProvider;
import com.biit.server.controller.SimpleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Collection;

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
        getProvider().save(reverse(eventDTO));
        return eventDTO;
    }

    @Override
    public Collection<EventDTO> create(Collection<EventDTO> eventDTOS, String creatorName) {
        eventDTOS.forEach(eventDTO -> eventDTO.setCreatedBy(creatorName));
        getProvider().saveAll(reverseAll(eventDTOS));
        return eventDTOS;
    }
}
