package aget.periodsbot.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class EaPeriodTest {
    @Test
    void start_whenDateIsPresent_returnsDate() {
        LocalDate current = LocalDate.now();
        Assertions.assertEquals(
                current,
                new EaPeriod(current).start()
        );
    }

    @Test
    void days_whenDateIsPresent_returnsDuration() {
        Assertions.assertEquals(
            new EaPeriod(
                LocalDate.now().minusDays(30)
            ).days(),
            30
        );
    }
}
