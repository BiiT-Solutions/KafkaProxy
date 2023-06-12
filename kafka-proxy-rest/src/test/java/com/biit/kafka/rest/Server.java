package com.biit.kafka.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan({"com.biit.kafka"})
@EnableJpaRepositories({"com.biit.kafka.persistence.repositories"})
@EntityScan({"com.biit.kafka.persistence.entities"})
@ComponentScan({"com.biit.kafka", "com.biit.usermanager.client", "com.biit.server.client"})
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
