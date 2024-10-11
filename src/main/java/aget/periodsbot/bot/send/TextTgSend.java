package aget.periodsbot.bot.send;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Objects;
import java.util.function.Supplier;

public class TextTgSend implements Supplier<SendMessage> {
    private final String text;
    private final String chatId;

    public TextTgSend(String text, String chatId) {
        this.text = text;
        this.chatId = chatId;
    }

    public SendMessage get() {
        return new SendMessage(text, chatId);
    }

    @Override
    public boolean equals(Object object) {
        return this == object ||
            object instanceof TextTgSend
                && this.text.equals(((TextTgSend) object).text)
                && this.chatId.equals(((TextTgSend) object).chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.chatId);
    }
}
