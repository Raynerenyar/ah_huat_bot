package zzb.telegram.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import zzb.telegram.bot.repository.ChatRepo;
import zzb.telegram.bot.repository.entities.Chat;

@SpringBootApplication(scanBasePackages = {
        "zzb.telegram.bot.service",
        "zzb.telegram.bot.repository",
        "zzb.telegram.bot.controller",
        "zzb.telegram.bot.config",
        "zzb.telegram.bot.cron"})
@EntityScan("zzb.telegram.bot.repository.entities")
@EnableScheduling
public class TotoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TotoApplication.class, args);

    }

}
