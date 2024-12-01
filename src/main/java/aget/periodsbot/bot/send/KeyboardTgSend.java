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

import java.util.Objects;
import java.util.function.Supplier;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public final class KeyboardTgSend implements Supplier<SendMessage> {
    private final String text;

    private final String chatId;

    private final ReplyKeyboardMarkup keyboardMarkup;

    public KeyboardTgSend(
        final String chatId,
        final String text,
        final ReplyKeyboardMarkup keyboardMarkup
    ) {
        this.chatId = chatId;
        this.text = text;
        this.keyboardMarkup = keyboardMarkup;
    }

    public SendMessage get() {
        final SendMessage sendMessage = new SendMessage(this.chatId, this.text);
        sendMessage.setReplyMarkup(this.keyboardMarkup);
        return sendMessage;
    }

    @Override
    public boolean equals(final Object object) {
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
