package aget.periodsbot.bot.command;

import aget.periodsbot.bot.FkUpdate;
import aget.periodsbot.bot.convert.StringToDateConvert;
import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.fake.FkPeriods;
import aget.periodsbot.domain.fake.FkTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

public class NewPeriodCmdTest {
    @Test
    void execute_PeriodIsNotPresent_addsNew() {
        Update update = new FkUpdate("test", 1L, "20-12-2021").update();
        FkTransaction transaction = new FkTransaction();
        transaction.consume(users -> users.add(1L, "test"));

        Assertions.assertEquals(
            new SendMsg(update, "Есть, мэм!"),
            new NewPeriodCmd(
                transaction,
                new StringToDateConvert("dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}")
            ).execute(update).orElseGet(() -> new SendMsg(update, "no"))
        );
        Assertions.assertEquals(
            new FkPeriods(LocalDate.of(2021, 12, 20)),
            transaction.callback(users -> users.user(1L).periods())
        );
    }
}
