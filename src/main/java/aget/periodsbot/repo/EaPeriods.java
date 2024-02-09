package aget.periodsbot.repo;

import aget.periodsbot.domain.EaPeriod;
import aget.periodsbot.domain.Period;
import org.jdbi.v3.core.Handle;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EaPeriods implements Periods {
    private final Handle dataSource;
    private final UUID userId;

    public EaPeriods(Handle dataSource, UUID userId) {
        this.dataSource = dataSource;
        this.userId = userId;
    }

    @Override
    public Period add(Date cycleStartDate) {
        return this.dataSource
                .registerRowMapper(EaPeriod.class,
                        (rs, ctx) -> new EaPeriod(UUID.fromString(rs.getString("id")), cycleStartDate))
                .inTransaction(handle ->
                        handle.createUpdate("INSERT INTO periods (user_id, start_date) VALUES (?, ?)")
                                .bind("user_id", userId)
                                .bind("start_date", cycleStartDate)
                                .executeAndReturnGeneratedKeys("id")
                                .mapTo(EaPeriod.class))
                .first();
    }

    @Override
    public Period getCurrentPeriod() {
        return null;
    }

    @Override
    public List<Period> lastPeriods(Long amount) {
        return null;
    }

    @Override
    public Integer averagePeriodLength() {
        return this.dataSource.inTransaction(handle ->
                        handle.select("SELECT AVG(DATE_PART('day', lead(periodDate) OVER (ORDER BY start_date) - periodDate - 1)) AS average_length " +
                                        "FROM periods WHERE user_id = ?")
                                .bind("user_id", this.userId)
                                .mapTo(Integer.class))
                .first();
    }
}
