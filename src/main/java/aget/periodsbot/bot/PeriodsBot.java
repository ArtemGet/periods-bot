package aget.periodsbot.bot;

import aget.periodsbot.bot.route.Route;
import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PeriodsBot extends TelegramLongPollingBot{
    private final String botName;
    private final String botToken;
    private final Route<Update, Send> route;

    public PeriodsBot(String botName,
                      String botToken,
                      Route<Update, Send> route){
        this.botName = botName;
        this.botToken = botToken;
        this.route = route;
    }

    @Override
    public void onUpdateReceived(Update update){
        this.route.route(update)
        .ifPresent(send -> send.send(this));
    }

    @Override
    public String getBotUsername(){
        return this.botName;
    }

    @Override
    public String getBotToken(){
        return this.botToken;
    }
}
