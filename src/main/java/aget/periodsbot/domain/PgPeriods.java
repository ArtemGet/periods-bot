package aget.periodsbot.domain;

import org.jdbi.v3.core.Handle;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PgPeriods implements Periods {
    private final Handle dataSource;
    private final UUID userId;

    public PgPeriods(Handle dataSource, UUID userId) {
        this.dataSource = dataSource;
        this.userId = userId;
    }

    @Override
    public void add(Date start) {
        this.dataSource
            .inTransaction(
                handle ->
                    handle.createUpdate("""
                            INSERT INTO public.periods (user_id, start_date)
                             VALUES (:user_id, :start_date)
                            """)
                        .bind("user_id", this.userId)
                        .bind("start_date", start)
            );
    }

    @Override
    public List<Period> last(Integer amount) {
        return this.dataSource.registerRowMapper(
                Period.class,
                (rs, ctx) -> new EaPeriod(rs.getDate("start_date"), rs.getDate("end_date"))
            ).select(
                """
                    SELECT start_date, end_date
                    FROM (
                        SELECT start_date,
                        lag(start_date) OVER(
                            ORDER BY start_date DESC,
                            start_date rows between current row and unbounded following 
                        ) as end_date
                        FROM public.periods
                        WHERE user_id = ?
                        limit ?
                    ) x""",
                this.userId
            ).setMaxRows(amount)
            .mapTo(Period.class)
            .collectIntoList();
    }

    @Override
    public void remove(Date start) {
        this.dataSource.inTransaction(
            handle ->
                handle.execute("""
                    DELETE FROM public.periods
                    WHERE user_id = ?
                    AND  start_date = ?""",
                    this.userId, start
                )
        );
    }
}
