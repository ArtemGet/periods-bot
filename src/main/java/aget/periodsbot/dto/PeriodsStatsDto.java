package aget.periodsbot.dto;

import java.util.Collection;

public class PeriodsStatsDto {
    private final Collection<PeriodStatsDto> periodsStats;
    private final LastPeriodStatsDto lastPeriod;
    private final Integer avgPeriodLength;

    public PeriodsStatsDto(Collection<PeriodStatsDto> periodsStats,
                           LastPeriodStatsDto lastPeriod,
                           Integer avgPeriodLength) {
        this.periodsStats = periodsStats;
        this.lastPeriod = lastPeriod;
        this.avgPeriodLength = avgPeriodLength;
    }

    public Collection<PeriodStatsDto> periodsStats() {
        return periodsStats;
    }

    public LastPeriodStatsDto lastPeriod() {
        return lastPeriod;
    }

    public Integer avgPeriodLength() {
        return avgPeriodLength;
    }
}
