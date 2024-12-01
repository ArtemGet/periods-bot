package aget.periodsbot.bot.command;

import aget.periodsbot.bot.FkUpdate;
import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.fake.FkTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class PeriodStatisticCmdTest {
    @Test
    void execute_periodsArePresent_ShowPeriods() {
        Update update = new FkUpdate().update();
        LocalDate last = LocalDate.of(2021, 12, 20);
        FkTransaction transaction = new FkTransaction();
        transaction.consume(users -> {
            users.add(1L, "test");
            users.user(1L).periods().add(last);
            users.user(1L).periods().add(last.minusDays(5));
            users.user(1L).periods().add(last.minusDays(10));
            users.user(1L).periods().add(last.minusDays(15));
        });

        Assertions.assertEquals(
            new SendMsg(update,
                "20.12.2021 - " + (DAYS.between(last, LocalDate.now())+1)
                    + "\n15.12.2021 - 6"
                    + "\n10.12.2021 - 6"),
            new PeriodStatisticCmd(
                transaction,
                3,
                "dd.MM.yyyy"
            ).execute(update).orElseGet(() -> new SendMsg(update, "no"))
        );
    }
}
