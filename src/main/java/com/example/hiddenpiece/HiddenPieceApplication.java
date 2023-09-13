package com.example.hiddenpiece;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HiddenPieceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiddenPieceApplication.class, args);
    }
}
