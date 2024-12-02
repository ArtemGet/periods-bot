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

import java.util.UUID;
import org.jdbi.v3.core.Handle;

/**
 * {@link User} for PostgreSQL database.
 *
 * @since 0.1.0
 */
public final class PgUser implements User {
    /**
     * Database connection.
     */
    private final Handle source;

    /**
     * Periods factory.
     */
    private final PeriodsFactory periods;

    /**
     * User id.
     */
    private final UUID id;

    public PgUser(
        final Handle source,
        final PeriodsFactory periods,
        final UUID id
    ) {
        this.source = source;
        this.periods = periods;
        this.id = id;
    }

    @Override
    public String name() {
        return this.source
            .inTransaction(
                handle ->
                    handle.select(
                        "SELECT name FROM public.users WHERE id = ?",
                        this.id
                    ).mapTo(String.class)
            ).findFirst()
            .orElse("пользователь");
    }

    @Override
    public Periods periods() {
        return this.periods.periods(this.source, this.id);
    }
}
