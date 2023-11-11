package org.telegram.toto.bots;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.toto.repository.TelegramRepo;
import org.telegram.toto.repository.entities.Chat;
import org.telegram.toto.service.WebscrapperService;

public class AhHuatBot extends AbilityBot {

    private TelegramRepo telegramRepo;
    private WebscrapperService webscrapperService;
    private long creatorId;
    private static final Logger logger = LoggerFactory.getLogger(AhHuatBot.class);

    public AhHuatBot(
            String BOT_TOKEN,
            String BOT_USERNAME,
            TelegramRepo telegramRepo,
            long creatorId,
            WebscrapperService webscrapperService) {
        super(BOT_TOKEN, BOT_USERNAME);
        this.telegramRepo = telegramRepo;
        this.creatorId = creatorId;
        this.webscrapperService = webscrapperService;
    }

    public AhHuatBot(String BOT_TOKEN, String BOT_USERNAME) {
        super(BOT_TOKEN, BOT_USERNAME);
    }

    @Override
    public long creatorId() {
        return creatorId;
    }

    public Ability nextDrawCommand() {
        return Ability
                .builder()
                .name("next")
                .info("Get the upcoming draw prize money and datetime.")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    silent.send(
                            webscrapperService.getNextDrawInString(),
                            ctx.chatId());
                })
                .build();
    }

    public Ability subscribeCommand() {
        return Ability
                .builder()
                .name("subscribe")
                .info("To subscribe to alerts on the next draw")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    Chat chat = new Chat();
                    chat.setChatId(String.valueOf(ctx.chatId()));
                    try {

                        String value;
                        if (ctx.arguments().length > 0 && (value = ctx.firstArg()) != null) {
                            chat.setAlertValue(Long.valueOf(value));
                        } else {
                            chat.setAlertValue(Long.valueOf(0));
                        }
                        telegramRepo.save(chat);

                        // responding after successful save there the need for another if statement

                        if (ctx.arguments().length > 0) {
                            silent.send("Thank you for subscribing with alert value of "
                                    + String.format("%,.0f", Double.valueOf(ctx.firstArg())), creatorId);
                        } else {
                            silent.send("Thank you for subscribing", ctx.chatId());
                        }
                    } catch (NumberFormatException | IllegalStateException e) {
                        if (e.getClass() == NumberFormatException.class)
                            silent.send("Number format is wrong", ctx.chatId());
                        logger.error(e.getMessage());
                    }
                })
                .enableStats()
                .build();
    }

    public Ability unsubscribeCommand() {
        return Ability
                .builder()
                .name("unsubscribe")
                .info("To unsubscribe from the alerts.")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> telegramRepo.deleteById(String.valueOf(ctx.chatId())))
                .post(ctx -> silent.send("You have unsubscribed", ctx.chatId()))
                .build();
    }

    public Ability prevCommand() {
        return Ability
                .builder()
                .name("prev")
                .info("Get previous draw")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {

                    String prevDraw = webscrapperService.getPreviousDraw();

                    SendMessage sendMessage = new SendMessage();

                    sendMessage.enableMarkdown(true);
                    sendMessage.setProtectContent(true);
                    sendMessage.setText(prevDraw);
                    sendMessage.setChatId(ctx.chatId());
                    try {
                        sender.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        logger.error(e.getMessage());
                    }

                })
                .build();
    }

}
