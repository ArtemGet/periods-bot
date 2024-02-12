package aget.periodsbot.dto;

import java.util.Date;

public class PeriodStatsDto {
    private final Date cycleStart;
    private final Date cycleEnd;

    public PeriodStatsDto(Date cycleStart, Date cycleEnd) {
        this.cycleStart = cycleStart;
        this.cycleEnd = cycleEnd;
    }

    public Date cycleStart() {
        return cycleStart;
    }

    public Date cycleEnd() {
        return cycleEnd;
    }
}
