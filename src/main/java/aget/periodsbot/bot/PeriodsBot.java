package aget.periodsbot.bot;

import aget.periodsbot.bot.route.Route;
import aget.periodsbot.bot.send.Send;
import aget.periodsbot.config.BotProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class PeriodsBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(PeriodsBot.class);
    private final BotProps props;
    private final TelegramBotsApi telegramBotsApi;
    private final Route<Update, Send> route;

    public PeriodsBot(BotProps props,
                      TelegramBotsApi telegramBotsApi,
                      Route<Update, Send> route) {
        this.props = props;
        this.telegramBotsApi = telegramBotsApi;
        this.route = route;
    }

    public PeriodsBot(BotProps props,
                      Route<Update, Send> route) throws TelegramApiException {
        this.props = props;
        this.telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        this.route = route;
    }

    @Override
    public void onUpdateReceived(Update update) {
        this.route.route(update)
                .ifPresent(send -> send.send(this));
    }

    @Override
    public String getBotUsername() {
        return this.props.botName();
    }

    @Override
    public String getBotToken() {
        return this.props.botToken();
    }

    public void start() {
        try {
            this.telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error("Error starting bot: {}", e.getMessage(), e);

            throw new RuntimeException(e);
        }
    }
}
