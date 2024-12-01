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

public class FkUser implements User {
    private final Long id;
    private final String name;
    private final Periods periods;

    public FkUser(Long id, String name, Periods periods) {
        this.id = id;
        this.name = name;
        this.periods = periods;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Periods periods() {
        return this.periods;
    }

    @Override
    public boolean equals(Object object) {
        return this == object ||
            object instanceof FkUser
                && this.id.equals(((FkUser) object).id)
                && this.name.equals(((FkUser) object).name)
                && this.periods.equals(((FkUser) object).periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.periods);
    }
}
