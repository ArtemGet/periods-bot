package aget.periodsbot.domain;

import java.util.Date;

public class EaPeriod implements Period {
    private final Date cycleStartDate;
    private static final long MILLIS_IN_DAY = 86400000L;

    public EaPeriod(Date cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    @Override
    public Date cycleStartDate() {
        return new Date(this.cycleStartDate.getTime());
    }

    @Override
    public Date cycleEndDate(Period nextPeriod) {
        long currentCycleStartTime = this.cycleStartDate.getTime();
        long nexCycleStartTime = nextPeriod.cycleStartDate().getTime();

        long currentCycleEndTime = nexCycleStartTime - MILLIS_IN_DAY;

        if (currentCycleEndTime < currentCycleStartTime) {
            return new Date(currentCycleStartTime);
        }

        return new Date(currentCycleEndTime);
    }

    @Override
    public Integer daysPassedFromCycleStart(Date date) {
        long currentCycleStartTime = this.cycleStartDate.getTime();
        long currentTime = date.getTime();

        long millisPassedFromCycleStart = currentTime - currentCycleStartTime;

        if (millisPassedFromCycleStart < 0) {
            //custom ex
            throw new RuntimeException();
        }

        //move ret type to custom Days
        return (int) (millisPassedFromCycleStart / MILLIS_IN_DAY);
    }

    @Override
    public Integer predictDaysBeforeCycleEnd(Date date, Integer avgCycleDayLength) {
        long currentCycleStartTime = this.cycleStartDate.getTime();
        long currentTime = date.getTime();

        long predictedCurrentCycleEndTime = currentCycleStartTime + avgCycleDayLength * MILLIS_IN_DAY;

        long millisBeforeCycleEnd = predictedCurrentCycleEndTime - currentTime;

        if (millisBeforeCycleEnd < 0) {
            //custom ex
            throw new RuntimeException();
        }

        return (int) millisBeforeCycleEnd / (int) MILLIS_IN_DAY;
    }
}
