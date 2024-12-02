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

/**
 * {@link Transaction} for PostgreSQL database.
 *
 * @since 0.1.0
 */
public final class PgTransaction implements Transaction<Users> {
    /**
     * Database connection.
     */
    private final Jdbi jdbi;

    /**
     * Periods factory.
     */
    private final PeriodsFactory periods;

    public PgTransaction(final String url) {
        this(Jdbi.create(url));
    }

    public PgTransaction(final Jdbi jdbi) {
        this(jdbi, new PgPeriodsFactory());
    }

    public PgTransaction(final Jdbi jdbi, final PeriodsFactory periods) {
        this.jdbi = jdbi;
        this.periods = periods;
    }

    @Override
    public <R> R callback(final Function<Users, R> function) {
        return this.jdbi.inTransaction(
            handle ->
                function.apply(new PgUsers(handle, this.periods))
        );
    }

    @Override
    public void consume(final Consumer<Users> consumer) {
        this.jdbi.useTransaction(
            handle ->
                consumer.accept(new PgUsers(handle, this.periods))
        );
    }
}
