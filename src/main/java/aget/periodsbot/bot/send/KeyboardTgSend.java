package aget.periodsbot.bot.send;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Objects;
import java.util.function.Supplier;

public class KeyboardTgSend implements Supplier<SendMessage> {
    private final String text;
    private final String chatId;
    private final ReplyKeyboardMarkup keyboardMarkup;

    public KeyboardTgSend(String chatId, String text, ReplyKeyboardMarkup keyboardMarkup) {
        this.chatId = chatId;
        this.text = text;
        this.keyboardMarkup = keyboardMarkup;
    }

    public SendMessage get() {
        SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

    @Override
    public boolean equals(Object object) {
        return this == object ||
            object instanceof KeyboardTgSend
                && this.text.equals(((KeyboardTgSend) object).text)
                && this.chatId.equals(((KeyboardTgSend) object).chatId)
                && this.keyboardMarkup.equals(((KeyboardTgSend) object).keyboardMarkup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.chatId, this.keyboardMarkup);
    }
}
