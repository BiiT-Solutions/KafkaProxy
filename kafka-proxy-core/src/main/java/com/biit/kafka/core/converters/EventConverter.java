package com.biit.kafka.core.converters;

import com.biit.kafka.core.converters.models.EventConverterRequest;
import com.biit.kafka.core.models.EventDTO;
import com.biit.kafka.events.Event;
import com.biit.server.controller.converters.ElementConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class EventConverter extends ElementConverter<Event, EventDTO, EventConverterRequest> {


    @Override
    protected EventDTO convertElement(EventConverterRequest from) {
        final EventDTO eventDTO = new EventDTO();
        BeanUtils.copyProperties(from.getEntity(), eventDTO);
        eventDTO.setMessageId(from.getEntity().getMessageId());
        eventDTO.setSessionId(from.getEntity().getSessionId());
        eventDTO.setCorrelationId(from.getEntity().getCorrelationId());
        return eventDTO;
    }

    @Override
    public Event reverse(EventDTO to) {
        if (to == null) {
            return null;
        }
        final Event event = new Event();
        BeanUtils.copyProperties(to, event);
        event.setMessageId(to.getMessageId());
        event.setSessionId(to.getSessionId());
        event.setCorrelationId(to.getCorrelationId());
        return event;
    }
}
