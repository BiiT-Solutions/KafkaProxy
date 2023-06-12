package com.biit.kafka.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource("classpath:application.properties")
@ComponentScan({"com.biit.kafka.core"})
public class TestConfiguration {

}
