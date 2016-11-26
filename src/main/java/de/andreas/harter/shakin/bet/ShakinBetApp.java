package de.andreas.harter.shakin.bet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ShakinBetApp {

    public static void main(String[] args) {
        SpringApplication.run(ShakinBetApp.class, args);
    }

}
