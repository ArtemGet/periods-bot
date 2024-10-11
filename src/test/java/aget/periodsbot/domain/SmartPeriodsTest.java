package aget.periodsbot.domain;

import aget.periodsbot.domain.fake.FkPeriods;
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
                    LocalDate.now().minusDays(5),
                    LocalDate.now().minusDays(15),
                    LocalDate.now().minusDays(25)
                )
            ).avgLength(5),
            10
        );
    }
}
