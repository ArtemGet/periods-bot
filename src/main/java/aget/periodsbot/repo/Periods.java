package aget.periodsbot.repo;

import aget.periodsbot.domain.Period;

import java.util.Date;
import java.util.List;

public interface Periods {
    Period add(Date cycleStartDate);

    Period getCurrentPeriod();

    List<Period> lastPeriods(Long amount);

    Integer averagePeriodLength();
}
