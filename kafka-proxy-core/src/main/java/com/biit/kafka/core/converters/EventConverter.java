package com.biit.kafka.core.converters;

import com.biit.kafka.core.converters.models.EventConverterRequest;
import com.biit.kafka.core.models.EventDTO;
import com.biit.kafka.core.models.StringEvent;
import com.biit.server.controller.converters.ElementConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class EventConverter extends ElementConverter<StringEvent, EventDTO, EventConverterRequest> {


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
    public StringEvent reverse(EventDTO to) {
        if (to == null) {
            return null;
        }
        final StringEvent event = new StringEvent();
        BeanUtils.copyProperties(to, event);
        event.setMessageId(to.getMessageId());
        event.setSessionId(to.getSessionId());
        event.setCorrelationId(to.getCorrelationId());
        return event;
    }
}
