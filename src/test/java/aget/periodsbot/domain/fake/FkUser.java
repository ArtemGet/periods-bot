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

import aget.periodsbot.domain.Periods;
import aget.periodsbot.domain.User;
import java.util.Objects;

/**
 * Fake user.
 *
 * @since 0.1.0
 */
public final class FkUser implements User {
    /**
     * Id.
     */
    private final Long id;

    /**
     * Name.
     */
    private final String username;

    /**
     * Periods.
     */
    private final Periods prds;

    public FkUser(final Long id, final String name, final Periods periods) {
        this.id = id;
        this.username = name;
        this.prds = periods;
    }

    @Override
    public String name() {
        return this.username;
    }

    @Override
    public Periods periods() {
        return this.prds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.username, this.prds);
    }

    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof FkUser
            && this.id.equals(((FkUser) object).id)
            && this.username.equals(((FkUser) object).username)
            && this.prds.equals(((FkUser) object).prds);
    }
}
