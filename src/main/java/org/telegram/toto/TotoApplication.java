package org.telegram.toto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.toto.bots.AhHuatBot;
import org.telegram.toto.repository.TelegramRepo;
import org.telegram.toto.service.WebscrapperService;

@SpringBootApplication(scanBasePackages = {
		"org.telegram.toto.service",
		"org.telegram.toto.repository",
		"org.telegram.toto.controller",
		"org.telegram.toto.config",
		"org.telegram.toto.cron" })
@EntityScan("org.telegram.toto.repository.entities")
@EnableScheduling
public class TotoApplication implements CommandLineRunner {

	// @Autowired
	// private WebscrapperService webscrapperService;
	// @Value("${bot.token}")
	// private String BOT_TOKEN;
	// @Value("${bot.username}")
	// private String BOT_USERNAME;
	// @Value("${creator.id}")
	// private long CREATOR_ID;
	// @Autowired
	// private TelegramRepo telegramRepo;
	// @Value("${site.url}")
	// private String url;

	public static void main(String[] args) {
		SpringApplication.run(TotoApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

	}

}
