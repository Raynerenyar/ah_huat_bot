package org.telegram.toto.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.toto.bots.AhHuatBot;
import org.telegram.toto.repository.TelegramRepo;
import org.telegram.toto.repository.entities.Chat;
import org.telegram.toto.service.WebscrapperService;

@Configuration
public class BotConfig {
    @Value("${bot.token}")
    private String BOT_TOKEN;
    @Value("${bot.username}")
    private String BOT_USERNAME;
    @Value("${creator.id}")
    private long CREATOR_ID;
    @Autowired
    private TelegramRepo telegramRepo;
    @Autowired
    private WebscrapperService webscrapperService;

    @Bean
    public AhHuatBot createBot() {

        TelegramBotsApi botsApi;
        AhHuatBot bot = new AhHuatBot(
                BOT_TOKEN,
                BOT_USERNAME,
                telegramRepo,
                CREATOR_ID,
                webscrapperService);
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return bot;
    }
}
