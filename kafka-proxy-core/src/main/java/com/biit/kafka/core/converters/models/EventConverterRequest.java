package com.biit.kafka.core.converters.models;

import com.biit.kafka.core.models.StringEvent;
import com.biit.server.converters.models.ConverterRequest;

public class EventConverterRequest extends ConverterRequest<StringEvent> {
    public EventConverterRequest(StringEvent event) {
        super(event);
    }
}
