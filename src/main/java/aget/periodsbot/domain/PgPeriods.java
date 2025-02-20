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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.jdbi.v3.core.Handle;

/**
 * {@link Periods} for PostgreSQL database.
 *
 * @since 0.1.0
 */
public final class PgPeriods implements Periods {
    /**
     * Database connection.
     */
    private final Handle source;

    /**
     * User id.
     */
    private final UUID id;

    public PgPeriods(final Handle source, final UUID id) {
        this.source = source;
        this.id = id;
    }

    @Override
    public void add(final LocalDate start) {
        this.source.useTransaction(
            handle ->
                handle
                    .createUpdate(
                        """
                        INSERT INTO public.periods (id, user_id, start_date)
                        VALUES (:id, :user_id, :start_date)
                        """)
                    .bind("id", UUID.randomUUID())
                    .bind("user_id", this.id)
                    .bind("start_date", start)
                    .execute()
        );
    }

    @Override
    public List<Period> last(final Integer amount) {
        return this.source
            .registerRowMapper(
                Period.class, (rs, ctx) ->
                    new EaPeriod(
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate()
                    )
            ).select(
                """
                SELECT start_date
                , CASE
                    WHEN end_date IS NULL THEN now()
                    ELSE (end_date - INTERVAL '1 DAY')
                  END AS end_date
                FROM (
                    SELECT start_date,
                    lag(start_date) OVER(
                        ORDER BY start_date DESC,
                        start_date rows between current row and unbounded following
                    ) as end_date
                    FROM public.periods
                    WHERE user_id = ?
                    LIMIT ?
                ) x""",
                this.id, amount
            ).mapTo(Period.class)
            .collectIntoList();
    }

    @Override
    public void remove(final LocalDate start) {
        this.source.useTransaction(
            handle ->
                handle.execute(
                    """
                    DELETE FROM public.periods
                    WHERE user_id = ?
                    AND  start_date = ?""",
                    this.id, start
                )
        );
    }
}
