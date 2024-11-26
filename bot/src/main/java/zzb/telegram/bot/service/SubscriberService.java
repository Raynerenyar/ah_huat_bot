package zzb.telegram.bot.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import zzb.telegram.bot.bots.AhHuatBot;
import zzb.telegram.bot.models.Draw;
import zzb.telegram.bot.repository.ChatRepo;
import zzb.telegram.bot.repository.entities.Chat;

@Service
@Transactional
public class SubscriberService {

    @Autowired
    private WebscrapperService webscrapperService;
    @Autowired
    private ChatRepo chatRepo;
    @Autowired
    @Lazy
    private AhHuatBot bot;
    private static final Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    public void saveChatPreferences(MessageContext ctx) {

        Chat chat = new Chat();
        chat.setChatId(String.valueOf(ctx.chatId()));
        try {

            String value = "";
            if (ctx.arguments().length > 0 && (value = ctx.firstArg()) != null) {
                chat.setAlertValue(Long.parseLong(value));
            } else {
                chat.setAlertValue(0L);
            }
            chatRepo.save(chat);

            // responding after successful save there the need for another if statement
            if (value != null && value.isBlank()) {
                bot.silent().send("Thank you for subscribing", ctx.chatId());
            } else {
                bot.silent().send("Thank you for subscribing with alert value of "
                        + String.format("$%,.0f", Double.valueOf(ctx.firstArg())), ctx.chatId());
            }
        } catch (NumberFormatException | IllegalStateException e) {
            if (e.getClass() == NumberFormatException.class)
                bot.silent().send("Number format is wrong", ctx.chatId());
            logger.error(e.getMessage(), e);
        }
    }

    public void notifySubscribers(List<String> chatIds, Draw draw) {

        chatIds.forEach(id -> {
            String drawInString = webscrapperService.getNextDrawInString(draw);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(id);
            sendMessage.setText(drawInString);
            try {
                bot.sender().execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.error("Chat id: " + id + " not found, deleting");
                chatRepo.deleteChat(id);
            }

        });

    }

    public void notifySubscribersSilently(List<String> chatIds, Draw draw) {
        chatIds.forEach(id -> {
            String drawInString = webscrapperService.getNextDrawInString(draw);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(id);
            StringBuilder sb = new StringBuilder();
            sb.append("==== The latest draw ====\n");
            sb.append(drawInString);
            sendMessage.setText(sb.toString());
            bot.silent().execute(sendMessage);
        });
    }

    public boolean initialNotifySubSilent(String chatId) {
        Optional<Draw> opt = webscrapperService.getNextDraw();

        if (opt.isPresent()) {
            Draw draw = opt.get();

            chatRepo.updateChatsNextDrawReceived(true, Collections.singletonList(chatId));
            String drawInString = webscrapperService.getNextDrawInString(draw);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            String sb = "===== Upcoming draw =====\n" + drawInString;

            sendMessage.setText(sb);
            bot.silent().execute(sendMessage);
            return true;
        }
        return false;
    }

    public void initialNotifySubLoud(List<String> chatIds, Draw draw) {

        for (String id : chatIds) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(id);
            StringBuilder sb = new StringBuilder();
            sb.append("==== Upcoming draw ====\n");
            String drawInString = webscrapperService.getNextDrawInString(draw);
            sb.append(drawInString);
            sendMessage.setText(sb.toString());
            try {
                bot.sender().execute(sendMessage);
            } catch (TelegramApiException e) {
                notifyFail(chatIds);
                logger.error(e.getMessage(), e);
            }
        }

    }

    public boolean subsequentNotifySubLoud(List<String> chatIds) {

        Optional<Draw> opt = webscrapperService.getNextDraw();
        if (opt.isPresent()) {

            Draw draw = opt.get();
            // List<String> chatIds =
            // chatRepo.findAllByAlertValueNextDrawReceivedTrue(draw.getValue());
            notifySubscribers(chatIds, draw);
            return true;

        } else {
            return false;
        }
    }

    public void subsequentNotifySubLoud(List<String> chatIds, Draw draw) {
        notifySubscribers(chatIds, draw);
    }

    public void notifyFail(List<String> chatId) {
        chatId.forEach(id -> {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(id);
            sendMessage.setText("Error occurred when getting next draw");
            try {
                bot.sender().execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public void notifyFailSilent(List<String> chatId) {
        chatId.forEach(id -> {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(id);
            sendMessage.setText("Error occurred when getting next draw");
            bot.silent().execute(sendMessage);
        });
    }
}
