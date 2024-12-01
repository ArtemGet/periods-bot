/*
 * MIT License
 *
 * Copyright (c) 2024 Artem Getmanskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
