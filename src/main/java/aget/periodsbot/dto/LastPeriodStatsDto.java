package aget.periodsbot.dto;

import java.time.LocalDate;
import java.util.Date;

public class LastPeriodStatsDto {
    private final LocalDate cycleStart;
    private final Integer daysPassedFromCycleStart;
    private final Integer predictedDaysBeforeCycleEnd;

    public LastPeriodStatsDto(LocalDate cycleStart,
                              Integer daysPassedFromCycleStart,
                              Integer predictedDaysBeforeCycleEnd) {
        this.cycleStart = cycleStart;
        this.daysPassedFromCycleStart = daysPassedFromCycleStart;
        this.predictedDaysBeforeCycleEnd = predictedDaysBeforeCycleEnd;
    }

    public LocalDate cycleStart() {
        return cycleStart;
    }

    public Integer daysPassedFromCycleStart() {
        return daysPassedFromCycleStart;
    }

    public Integer predictedDaysBeforeCycleEnd() {
        return predictedDaysBeforeCycleEnd;
    }
}
