package aget.periodsbot.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class EaPeriod implements Period {
    private final LocalDate start;
    private final LocalDate end;

    public EaPeriod(LocalDate start) {
        this(start, LocalDate.now());
    }

    public EaPeriod(LocalDate start, Period next) {
        this(start, next.start());
    }

    public EaPeriod(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public LocalDate start() {
        return start;
    }

    @Override
    public Integer days() {
        return Long
            .valueOf(Math.abs(ChronoUnit.DAYS.between(start,end)))
            .intValue() + 1;
    }
}
