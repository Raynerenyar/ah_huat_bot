package org.telegram.toto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "org.telegram.toto.service",
        "org.telegram.toto.repository",
        "org.telegram.toto.controller",
        "org.telegram.toto.config" })
@EntityScan("org.telegram.toto.repository.entities")
@EnableScheduling
public class TotoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TotoApplication.class, args);

    }

}
