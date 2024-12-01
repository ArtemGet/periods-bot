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

package aget.periodsbot.domain;

import java.util.function.Consumer;
import java.util.function.Function;
import org.jdbi.v3.core.Jdbi;

public final class PgTransaction implements Transaction<Users> {
    private final Jdbi db;

    private final PeriodsFactory periodsFactory;

    public PgTransaction(final String url) {
        this(Jdbi.create(url));
    }

    public PgTransaction(final Jdbi db) {
        this(db, new PgPeriodsFactory());
    }

    public PgTransaction(final Jdbi db, final PeriodsFactory periodsFactory) {
        this.db = db;
        this.periodsFactory = periodsFactory;
    }

    @Override
    public <R> R callback(final Function<Users, R> fn) {
        return this.db.inTransaction(handle ->
            fn.apply(new PgUsers(handle, this.periodsFactory))
        );
    }

    @Override
    public void consume(final Consumer<Users> fn) {
        this.db.useTransaction(handle ->
            fn.accept(new PgUsers(handle, this.periodsFactory))
        );
    }
}
