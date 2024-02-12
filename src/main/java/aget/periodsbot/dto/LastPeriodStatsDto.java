package aget.periodsbot.dto;

import java.util.Date;

public class LastPeriodStatsDto {
    private final Date cycleStart;
    private final Integer daysPassedFromCycleStart;
    private final Integer predictedDaysBeforeCycleEnd;

    public LastPeriodStatsDto(Date cycleStart,
                              Integer daysPassedFromCycleStart,
                              Integer predictedDaysBeforeCycleEnd) {
        this.cycleStart = cycleStart;
        this.daysPassedFromCycleStart = daysPassedFromCycleStart;
        this.predictedDaysBeforeCycleEnd = predictedDaysBeforeCycleEnd;
    }

    public Date cycleStart() {
        return cycleStart;
    }

    public Integer daysPassedFromCycleStart() {
        return daysPassedFromCycleStart;
    }

    public Integer predictedDaysBeforeCycleEnd() {
        return predictedDaysBeforeCycleEnd;
    }
}
