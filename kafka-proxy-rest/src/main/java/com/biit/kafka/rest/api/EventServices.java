package com.biit.kafka.rest.api;

import com.biit.kafka.core.controllers.EventController;
import com.biit.kafka.core.converters.EventConverter;
import com.biit.kafka.core.converters.models.EventConverterRequest;
import com.biit.kafka.core.models.EventDTO;
import com.biit.kafka.core.models.StringEvent;
import com.biit.kafka.core.providers.EventProvider;
import com.biit.server.rest.SimpleServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventServices extends SimpleServices<StringEvent, EventDTO, EventProvider, EventConverterRequest, EventConverter, EventController> {

    public EventServices(EventController controller) {
        super(controller);
    }
}
