package aget.periodsbot.dto;

import java.time.LocalDate;
import java.util.Date;

public class PeriodStatsDto {
    private final LocalDate cycleStart;

    public PeriodStatsDto(LocalDate cycleStart) {
        this.cycleStart = cycleStart;
    }

    public LocalDate cycleStart() {
        return cycleStart;
    }

}
