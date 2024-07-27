package aget.periodsbot.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class SmartPeriodsTest {
    @Test
    void current_periodsIsPresent_returnsCurrent() {
        LocalDate current = LocalDate.now();

        Assertions.assertEquals(
            new Periods.SmartPeriods(
                new FkPeriods(
                    current.minusDays(20),
                    current.minusDays(5),
                    current.minusDays(10),
                    current
                )
            ).current().start(),
            current
        );
    }

    @Test
    void avgLength_periodsIsPresent_returnsLength() {
        Assertions.assertEquals(
            new Periods.SmartPeriods(
                new FkPeriods(
                    LocalDate.now().minusDays(20),
                    LocalDate.now().minusDays(30),
                    LocalDate.now().minusDays(10)
                )
            ).avgLength(5),
            10
        );
    }
}
