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

package aget.periodsbot.bot;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * Fake update.
 *
 * @since 0.1.0
 */
public final class FkUpdate {
    /**
     * Name.
     */
    private final String username;

    /**
     * Id.
     */
    private final Long id;

    /**
     * Message.
     */
    private final String text;

    public FkUpdate() {
        this("text");
    }

    public FkUpdate(final String text) {
        this("test", 1L, text);
    }

    public FkUpdate(final String username, final Long id, final String text) {
        this.username = username;
        this.id = id;
        this.text = text;
    }

    public Update update() {
        final User user = new User();
        user.setUserName(this.username);
        user.setId(this.id);
        final Message message = new Message();
        message.setFrom(user);
        message.setText(this.text);
        final Update update = new Update();
        update.setMessage(message);
        return update;
    }
}
