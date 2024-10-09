package org.telegram.toto.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.toto.bots.AhHuatBot;
import org.telegram.toto.repository.ChatRepo;
import org.telegram.toto.service.CalculatePrizeService;
import org.telegram.toto.service.SubscriberService;
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
    private ChatRepo telegramRepo;
    @Autowired
    private WebscrapperService webscrapperService;
    @Autowired
    private CalculatePrizeService calculatePrizeService;
    @Autowired
    private SubscriberService subscriberService;

    private static final Logger logger = LoggerFactory.getLogger(BotConfig.class);

    @Bean
    public AhHuatBot createBot() {

        TelegramBotsApi botsApi;
        AhHuatBot bot = new AhHuatBot(
                BOT_TOKEN,
                BOT_USERNAME,
                telegramRepo,
                CREATOR_ID,
                webscrapperService,
                calculatePrizeService,
                subscriberService);
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            logger.error(e.getMessage(), e);
        }

        return bot;
    }
}
