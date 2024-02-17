package aget.periodsbot.repo;

import aget.periodsbot.domain.EaPeriod;
import aget.periodsbot.domain.Period;
import lombok.EqualsAndHashCode;
import org.jdbi.v3.core.Handle;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode
public class PgPeriods implements Periods {
    private final Handle dataSource;
    private final UUID userId;

    public PgPeriods(Handle dataSource, UUID userId) {
        this.dataSource = dataSource;
        this.userId = userId;
    }

    @Override
    public Period add(Date cycleStartDate) {
        return this.dataSource.registerRowMapper(
                EaPeriod.class,
                (rs, ctx) -> new EaPeriod(cycleStartDate)
        ).inTransaction(
                handle ->
                        handle.createUpdate("""
                                        INSERT INTO public.periods (user_id, start_date)
                                         VALUES (:user_id, :start_date)
                                        """)
                                .bind("user_id", this.userId)
                                .bind("start_date", cycleStartDate)
                                //remove
                                .executeAndReturnGeneratedKeys("id")
                                .mapTo(EaPeriod.class)
        ).first();
    }

    @Override
    public Period getCurrentPeriod() {
        return this.dataSource.registerRowMapper(
                        EaPeriod.class,
                        (rs, ctx) -> new EaPeriod(rs.getDate("start_date"))
                ).select(
                        "SELECT id,start_date FROM periods WHERE id = ? ORDER BY start_date DESC limit 1",
                        this.userId
                ).mapTo(EaPeriod.class)
                .first();
    }

    @Override
    public List<Period> lastPeriods(Long amount) {
        return this.dataSource.registerRowMapper(
                        Period.class,
                        (rs, ctx) -> new EaPeriod(rs.getDate("start_date"))
                ).select(
                        "SELECT id,start_date FROM public.periods WHERE id = ? ORDER BY start_date DESC limit ?",
                        this.userId
                ).setMaxRows(Math.toIntExact(amount))
                .mapTo(Period.class)
                .collectIntoList();
    }

    @Override
    public Integer averagePeriodLength() {
        return this.dataSource.inTransaction(
                handle ->
                        handle.select("""
                                        SELECT AVG(DATE_PART('day', lead(periodDate)
                                        OVER (ORDER BY start_date) - periodDate - 1)) AS average_length
                                        FROM public.periods WHERE user_id = ?
                                        """,
                                this.userId
                        ).mapTo(Integer.class)
        ).first();
    }
}
