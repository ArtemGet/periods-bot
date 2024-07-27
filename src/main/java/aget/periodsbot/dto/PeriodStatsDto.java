package aget.periodsbot.dto;

import java.util.Date;

public class PeriodStatsDto {
    private final Date cycleStart;

    public PeriodStatsDto(Date cycleStart) {
        this.cycleStart = cycleStart;
    }

    public Date cycleStart() {
        return cycleStart;
    }

}
