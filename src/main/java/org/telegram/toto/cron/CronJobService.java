package org.telegram.toto.cron;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.toto.bots.AhHuatBot;
import org.telegram.toto.models.Draw;
import org.telegram.toto.repository.TelegramRepo;
import org.telegram.toto.repository.entities.Chat;
import org.telegram.toto.service.WebscrapperService;

@Service
public class CronJobService {

    @Autowired
    private WebscrapperService webscrapperService;
    @Autowired
    private AhHuatBot bot;
    @Autowired
    private TelegramRepo telegramRepo;

    private static final Logger logger = LoggerFactory.getLogger(CronJobService.class);

    private boolean nextDrawReceived = false;
    private boolean moreThanCondition = false;

    // @Scheduled(cron = "*/10 * * * * *", zone = "Asia/Singapore")
    public void notifyDraw() {
        logger.info("cron job started");
        Optional<Draw> opt = webscrapperService.getNextDraw();

        if (opt.isPresent()) {
            List<Chat> chats = telegramRepo.findAll();
            String draw = webscrapperService.getNextDrawInString();

            for (Chat chat : chats) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chat.getChatId());
                sendMessage.setText(draw);
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    // Monday and Thursday after 6:30 PM every hour until a draw is released
    @Scheduled(cron = "0 30 18/1 ? * MON,THU", zone = "Asia/Singapore")
    public void runOnMondayAndThursdayAfter630PM() {
        if (!nextDrawReceived) {

            System.out.println("Scheduled task executed on Monday or Thursday after 6:30 PM");

            Optional<Draw> opt = webscrapperService.getNextDraw();

            opt.ifPresentOrElse(draw -> {

                List<Chat> chats = telegramRepo.findAll();

                long value = draw.getValue();

                // if value >= 5,000,000 then push notification
                if (value >= 5000000) {
                    moreThanCondition = true;
                    String drawInString = webscrapperService.getNextDrawInString(draw);

                    for (Chat chat : chats) {
                        if (nextDrawReceived)
                            break;
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(chat.getChatId());
                        sendMessage.setText(drawInString);
                        try {
                            bot.execute(sendMessage);
                        } catch (TelegramApiException e) {
                            logger.error(e.getMessage());
                        }
                    }
                }
                // Draw has been released, switch to running every day
                nextDrawReceived = true;

            }, () -> logger.info("Next draw has not been released."));

        }
    }

    // repeat it everyday at 11am
    @Scheduled(cron = "0 0 11 * * ?", zone = "Asia/Singapore")
    public void runEveryDay() {

        // if draw was previously received and moreThanCondition is true
        if (nextDrawReceived && moreThanCondition) {

            Optional<Draw> opt = webscrapperService.getNextDraw();
            LocalDateTime drawDate = opt.get().getDatetime();

            if (LocalDateTime.now().isAfter(drawDate)) {
                nextDrawReceived = false;
                moreThanCondition = false;
            }
        }
    }

}
