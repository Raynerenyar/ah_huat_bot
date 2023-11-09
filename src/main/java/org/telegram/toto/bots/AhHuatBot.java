package org.telegram.toto.bots;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.toto.models.Draw;
import org.telegram.toto.repository.TelegramRepo;
import org.telegram.toto.repository.entities.Chat;
import org.telegram.toto.service.WebscrapperService;

public class AhHuatBot extends AbilityBot {

    private TelegramRepo telegramRepo;
    private WebscrapperService webscrapperService;
    private long creatorId;

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
                    silent.send(webscrapperService.getNextDrawInString(), ctx.chatId());
                })
                .build();
    }

    public Ability subscribeCommand() {
        return Ability
                .builder()
                .name("subscribe")
                .info("To subscribe to alerts on the next draw that has prize money >= $5,000,000.")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    Chat chat = new Chat();
                    chat.setChatId(String.valueOf(ctx.chatId()));
                    telegramRepo.save(chat);
                })
                .post(ctx -> {
                    silent.send("Thank you for subscribing", ctx.chatId());
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
                .action(ctx -> {
                    telegramRepo.deleteById(String.valueOf(ctx.chatId()));
                })
                .post(ctx -> {
                    silent.send("You have unsubscribed", ctx.chatId());
                })
                .build();
    }

}
