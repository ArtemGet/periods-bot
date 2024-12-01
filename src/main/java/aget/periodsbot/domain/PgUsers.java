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

public final class PgUsers implements Users {
    private final Handle dataSource;

    private final PeriodsFactory periodsFactory;

    public PgUsers(final Handle dataSource, final PeriodsFactory periodsFactory) {
        this.dataSource = dataSource;
        this.periodsFactory = periodsFactory;
    }

    @Override
    public void add(final Long userTelegramId, final String name) {
        this.dataSource.useTransaction(
            handle ->
                handle.createUpdate("INSERT INTO public.users (id, t_id, name) VALUES (:id, :t_id, :name)")
                    .bind("id", UUID.randomUUID())
                    .bind("t_id", userTelegramId)
                    .bind("name", name)
                    .execute()
        );
    }

    @Override
    public User user(final Long userTelegramId) {
        return this.dataSource.registerRowMapper(
            PgUser.class,
            (rs, ctx) ->
                new PgUser(
                    this.dataSource,
                    this.periodsFactory,
                    UUID.fromString(rs.getString("id"))
                )
        ).inTransaction(
            handle ->
                handle.select(
                    "SELECT id,name FROM public.users WHERE t_id = ?",
                    userTelegramId
                ).mapTo(PgUser.class)
        ).first();
    }
}
