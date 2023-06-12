package com.biit.kafka.core.models;

import com.biit.kafka.events.Event;
import com.fasterxml.jackson.core.type.TypeReference;

public class StringEvent extends Event<String> {

    @Override
    protected TypeReference<String> getJsonParser() {
        return new TypeReference<>() {
        };
    }
}
