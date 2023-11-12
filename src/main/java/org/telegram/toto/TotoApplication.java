package org.telegram.toto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.toto.repository.ChatRepo;
import org.telegram.toto.repository.entities.Chat;

@SpringBootApplication(scanBasePackages = {
		"org.telegram.toto.service",
		"org.telegram.toto.repository",
		"org.telegram.toto.controller",
		"org.telegram.toto.config",
		"org.telegram.toto.cron" })
@EntityScan("org.telegram.toto.repository.entities")
@EnableScheduling
public class TotoApplication implements CommandLineRunner {

	@Autowired
	private ChatRepo chatRepo;

	public static void main(String[] args) {
		SpringApplication.run(TotoApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		List<String> chats1 = chatRepo.findAllByAlertValueNextDrawReceivedFalse(1000000);
		List<String> chats2 = chatRepo.findAllByAlertValueNextDrawReceivedTrue(1000000);

		for (String chat : chats1) {
			System.out.println(chat);
		}
		for (String chat : chats2) {
			System.out.println(chat);
		}
	}

}
