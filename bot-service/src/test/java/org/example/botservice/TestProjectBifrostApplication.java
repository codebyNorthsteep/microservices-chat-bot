package org.example.botservice;

import org.springframework.boot.SpringApplication;

public class TestProjectBifrostApplication {

    public static void main(String[] args) {
        SpringApplication.from(BotServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
