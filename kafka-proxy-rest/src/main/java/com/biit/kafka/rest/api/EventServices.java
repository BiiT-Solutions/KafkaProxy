package com.biit.kafka.rest.api;

import com.biit.kafka.core.controllers.EventController;
import com.biit.kafka.core.converters.EventConverter;
import com.biit.kafka.core.converters.models.EventConverterRequest;
import com.biit.kafka.core.models.EventDTO;
import com.biit.kafka.core.models.StringEvent;
import com.biit.kafka.core.providers.EventProvider;
import com.biit.server.rest.SimpleServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/events")
public class EventServices extends SimpleServices<StringEvent, EventDTO, EventProvider, EventConverterRequest, EventConverter, EventController> {

    public EventServices(EventController controller) {
        super(controller);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Sends an event.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/topics/{topic}")
    public void send(
            @Parameter(description = "Topic to be used. If not set will used the one defined in the 'applications.properties' file.")
            @PathVariable("topic") String topic,
            @Parameter(name = "key", required = false) @RequestParam(value = "key", required = false) String key,
            @Parameter(name = "partition", required = false) @RequestParam(value = "partition", required = false) Integer partition,
            @Parameter(name = "timestamp", required = false) @RequestParam(value = "timestamp", required = false) Long timestamp,
            @RequestBody EventDTO dto, Authentication authentication, HttpServletRequest request) {
        getController().create(topic, key, partition, timestamp, dto, authentication.getName());
    }

    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Sends a collection of events.", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/topics/{topic}/all")
    public void sendAll(
            @Parameter(description = "Topic to be used. If not set will used the one defined in the 'applications.properties' file.")
            @PathVariable("topic") String topic,
            @Parameter(name = "key", required = false) @RequestParam(value = "key", required = false) String key,
            @Parameter(name = "partition", required = false) @RequestParam(value = "partition", required = false) Integer partition,
            @Parameter(name = "timestamp", required = false) @RequestParam(value = "timestamp", required = false) Long timestamp,
            @RequestBody Collection<EventDTO> dtos, Authentication authentication, HttpServletRequest request) {
        getController().create(topic, key, partition, timestamp, dtos, authentication.getName());
    }

    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Reads old event. Note that Kafka only stores events for a maximum of one week.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/topics/{topics}/startingTime/{startingTime}/duration/{duration}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<EventDTO> get(
            @Parameter(description = "Topics to be used. If not set will used the one defined in the 'applications.properties'.")
            @PathVariable("topics") @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss") Collection<String> topics,
            @Parameter(description = "The search starting range (format: yyyy-MM-dd hh:mm:ss).", required = true)
            @PathVariable("startingTime") LocalDateTime startingTime,
            @Parameter(description = "Total duration of the range in seconds. If not set, the range will be considered until now.")
            @PathVariable("duration") Long duration,
            Authentication authentication, HttpServletRequest request) {
        if (duration == null) {
            duration = ChronoUnit.SECONDS.between(startingTime, LocalDateTime.now());
        }
        return getController().get(topics, startingTime, Duration.of(duration, TimeUnit.SECONDS.toChronoUnit()));
    }
}
