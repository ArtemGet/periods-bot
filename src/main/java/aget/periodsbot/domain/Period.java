package aget.periodsbot.domain;

import java.util.Date;

public interface Period {

    Date cycleStartDate();

    Date cycleEndDate(Period nextPeriod);

    Integer daysPassedFromCycleStart(Date date);

    Integer predictDaysBeforeCycleEnd(Date date, Integer avgCycleLength);
}
