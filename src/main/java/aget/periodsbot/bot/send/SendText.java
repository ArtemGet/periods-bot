package aget.periodsbot.bot.send;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SendText implements Send {
    private final SendMessage sendMessage;

    public SendText(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    @Override
    public void send(AbsSender send) {
        try {
            send.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
