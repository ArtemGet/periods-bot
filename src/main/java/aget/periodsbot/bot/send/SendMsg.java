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

import com.github.artemget.teleroute.send.Send;
import java.util.Objects;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Send message to chat.
 *
 * @since 0.1.0
 */
public final class SendMsg implements Send<AbsSender> {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SendMsg.class);

    /**
     * Telegram message provider.
     */
    private final Supplier<SendMessage> tgsend;

    public SendMsg(final Update update, final String text) {
        this(new TextTgSend(update.getMessage().getFrom().getId().toString(), text));
    }

    public SendMsg(final String id, final String text) {
        this(new TextTgSend(id, text));
    }

    public SendMsg(final Supplier<SendMessage> send) {
        this.tgsend = send;
    }

    @Override
    public void send(final AbsSender send) {
        try {
            send.execute(this.tgsend.get());
        } catch (final TelegramApiException exception) {
            LOG.error(
                "Error sending message to chat: {}",
                this.tgsend.get().getChatId(),
                exception
            );
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tgsend);
    }

    @Override
    public boolean equals(final Object object) {
        return this == object
            || object instanceof SendMsg && this.tgsend.equals(((SendMsg) object).tgsend);
    }
}
