package aget.periodsbot.domain;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EaPeriod implements Period {
    private final Date start;
    private final Date end;

    public EaPeriod(Date start) {
        this(start, new Date());
    }

    public EaPeriod(Date start, Period next) {
        this(start, next.start());
    }

    public EaPeriod(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Date start() {
        return start;
    }

    @Override
    public Integer days() {
        return Long.valueOf(
            TimeUnit.DAYS.convert(
                Math.abs(end.getTime() - start.getTime()),
                TimeUnit.MILLISECONDS
            )
        ).intValue();
    }
}
