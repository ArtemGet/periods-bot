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
 * {@link Users} for PostgreSQL.
 *
 * @since 0.1.0
 */
public final class PgUsers implements Users {
    /**
     * Database connection.
     */
    private final Handle source;

    /**
     * Periods factory.
     */
    private final PeriodsFactory periods;

    public PgUsers(final Handle source, final PeriodsFactory periods) {
        this.source = source;
        this.periods = periods;
    }

    @Override
    public void add(final Long id, final String name) {
        this.source.useTransaction(
            handle ->
                handle
                    .createUpdate(
                        "INSERT INTO public.users (id, t_id, name) VALUES (:id, :t_id, :name)"
                    )
                    .bind("id", UUID.randomUUID())
                    .bind("t_id", id)
                    .bind("name", name)
                    .execute()
        );
    }

    @Override
    public User user(final Long id) {
        return this.source.registerRowMapper(
            PgUser.class,
            (rs, ctx) ->
                new PgUser(
                    this.source,
                    this.periods,
                    UUID.fromString(rs.getString("id"))
                )
        ).inTransaction(
            handle ->
                handle.select(
                    "SELECT id,name FROM public.users WHERE t_id = ?",
                    id
                ).mapTo(PgUser.class)
        ).first();
    }
}
