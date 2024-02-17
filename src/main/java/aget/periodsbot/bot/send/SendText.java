package aget.periodsbot.bot.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SendText implements Send {
    private static final Logger log = LoggerFactory.getLogger(SendText.class);
    private final SendMessage sendMessage;

    public SendText(final SendMessage sendMessage) {
        this.sendMessage = sendMessage;
    }

    @Override
    public void send(AbsSender send) {
        try {
            send.execute(this.sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error sending message to chat: {}", this.sendMessage.getChatId(), e);
        }
    }
}
