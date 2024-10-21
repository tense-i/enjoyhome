package com.enjoyhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScans(value = {
        @ComponentScan("com.enjoyhome.config"),
        @ComponentScan("com.enjoyhome.controller"),
        @ComponentScan("com.enjoyhome.service")
})
public class EnjoyhomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnjoyhomeApplication.class, args);
    }
}
