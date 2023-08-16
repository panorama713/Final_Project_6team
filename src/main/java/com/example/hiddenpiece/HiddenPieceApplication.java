package com.example.hiddenpiece;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HiddenPieceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiddenPieceApplication.class, args);
    }

}
