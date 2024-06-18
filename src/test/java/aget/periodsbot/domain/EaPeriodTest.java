package aget.periodsbot.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class EaPeriodTest {
    private static final long MILLIS_IN_DAY = 86400000L;
    private static final long DAYS_IN_CYCLE = 30;

    @Test
    void cycleStartDate_shouldReturn_whenDateIsPresent() {
        Date current = new Date();

        Assertions.assertEquals(
                current,
                new EaPeriod(current).start()
        );
    }

    @Test
    void cycleEndDate_shouldReturn_whenNextPeriodInFuture() {
        Date current = new Date();
        Date previous = new Date(current.getTime() - DAYS_IN_CYCLE * MILLIS_IN_DAY);

        EaPeriod currentPeriod = new EaPeriod(current);
        EaPeriod previousPeriod = new EaPeriod(previous, current);

        Assertions.assertEquals(
                new Date(current.getTime() - MILLIS_IN_DAY),
                previousPeriod.days()
        );
    }
}
