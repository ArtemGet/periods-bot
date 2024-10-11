package aget.periodsbot.bot.send;

import com.github.artemget.teleroute.send.Send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;
import java.util.function.Supplier;

public class SendMsg implements Send<AbsSender> {
    private static final Logger log = LoggerFactory.getLogger(SendMsg.class);
    private final Supplier<SendMessage> tgSend;

    public SendMsg(Update update, String text) {
        this(new TextTgSend(update.getMessage().getFrom().getId().toString(), text));
    }

    public SendMsg(String chatId, String text) {
        this(new TextTgSend(chatId, text));
    }

    public SendMsg(Supplier<SendMessage> tgSend){
        this.tgSend = tgSend;
    }

    @Override
    public void send(AbsSender send){
        try {
            send.execute(this.tgSend.get());
        } catch (TelegramApiException e) {
            log.error("Error sending message to chat: {}", this.tgSend.get().getChatId(), e);
        }
    }

    @Override
    public boolean equals(Object object) {
        return this == object
            || object instanceof SendMsg && this.tgSend.equals(((SendMsg) object).tgSend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tgSend);
    }
}
