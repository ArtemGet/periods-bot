package aget.periodsbot.bot.command;

import aget.periodsbot.bot.FkUpdate;
import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.fake.FkTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CurrentPeriodCmdTest {
    @Test
    void execute_periodsArePresent_ShowCurrentPeriods() {
        Update update = new FkUpdate().update();
        LocalDate last = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        FkTransaction transaction = new FkTransaction();
        transaction.consume(users -> {
            users.add(1L, "test");
            users.user(1L).periods().add(last.minusDays(5));
            users.user(1L).periods().add(last.minusDays(35));
            users.user(1L).periods().add(last.minusDays(65));
        });

        Assertions.assertEquals(
            new SendMsg(update,
                String.format("Началось: %s\nДень: %s\nОсталось: %s", last.minusDays(5).format(formatter), 5, 25)),
            new CurrentPeriodCmd(
                transaction,
                formatter
            ).execute(update).orElseGet(() -> new SendMsg(update, "no"))
        );
    }
}
