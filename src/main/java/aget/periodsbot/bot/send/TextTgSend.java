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
