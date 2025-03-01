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

package aget.periodsbot.domain.fake;

import aget.periodsbot.domain.User;
import aget.periodsbot.domain.Users;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Fake users.
 *
 * @since 0.1.0
 */
public final class FkUsers implements Users {
    /**
     * Collection of users.
     */
    private final Dictionary<Long, User> users;

    public FkUsers() {
        this(new Hashtable<>());
    }

    public FkUsers(final Dictionary<Long, User> users) {
        this.users = users;
    }

    @Override
    public void add(final Long id, final String name) {
        this.users.put(id, new FkUser(id, name, new FkPeriods()));
    }

    @Override
    public User user(final Long id) {
        return this.users.get(id);
    }
}
