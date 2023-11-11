package org.telegram.toto.cron;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.checkerframework.checker.units.qual.s;
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

    public CronJobService(WebscrapperService webscrapperService, TelegramRepo telegramRepo2) {
        this.webscrapperService = webscrapperService;
    }

    private static final Logger logger = LoggerFactory.getLogger(CronJobService.class);

    private boolean nextDrawReceived = false;
    private boolean moreThanCondition = false;

    // Monday and Thursday after 6:30 PM every hour until a draw is released
    @Scheduled(cron = "0 30 18/1 ? * MON,THU", zone = "Asia/Singapore")
    public void runOnMondayAndThursdayAfter630PM() {
        if (!this.nextDrawReceived) {

            Optional<Draw> opt = webscrapperService.getNextDraw();
            if (opt.isPresent()) {
                Draw draw = opt.get();
                if (LocalDateTime.now().isBefore(draw.getDatetime())) {
                    sendDraw(opt.get()); // on success this.nextDrawReceived is true
                }
            }

        }
    }

    // repeat it everyday at 11am
    @Scheduled(cron = "0 0 11 * * ?", zone = "Asia/Singapore")
    public void runEveryDay() {

        // if draw was previously received and moreThanCondition is true
        if (this.nextDrawReceived && this.moreThanCondition) {

            Optional<Draw> opt = webscrapperService.getNextDraw();
            if (opt.isPresent()) {

                LocalDateTime drawDate = opt.get().getDatetime();

                if (LocalDateTime.now().isAfter(drawDate)) {
                    this.nextDrawReceived = false;
                    this.moreThanCondition = false;
                } else {
                    sendDraw(opt.get());
                }

            }
        }
        if (!this.nextDrawReceived) {
            logger.info("Error occured when getting next draw");
            List<Chat> chats = telegramRepo.findAll();
            SendMessage sendMessage = new SendMessage();

            for (Chat chat : chats) {

                sendMessage.setChatId(chat.getChatId());
                sendMessage.setText("Error occured when getting next draw");

                try {

                    bot.execute(sendMessage);

                } catch (TelegramApiException e) {

                    logger.error(e.getMessage());

                }
            }
        }
    }

    private void sendDraw(Draw draw) {

        List<Chat> chats = telegramRepo.findAll();
        SendMessage sendMessage = new SendMessage();

        long value = draw.getValue();

        // if value >= subscriber alert_value then push notification
        for (Chat chat : chats) {

            if (value >= chat.getAlertValue()) {
                this.moreThanCondition = true;
                String drawInString = webscrapperService.getNextDrawInString(draw);

                sendMessage.setChatId(chat.getChatId());
                sendMessage.setText(drawInString);

                try {

                    bot.execute(sendMessage);
                    this.nextDrawReceived = true;

                } catch (TelegramApiException e) {

                    logger.error(e.getMessage());

                }
            }
        }
    }

}
