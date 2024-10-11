package aget.periodsbot.bot.command;

import aget.periodsbot.bot.FkUpdate;
import aget.periodsbot.bot.convert.StringToDateConvert;
import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.fake.FkPeriods;
import aget.periodsbot.domain.fake.FkTransaction;
import aget.periodsbot.domain.fake.FkUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

public class RemovePeriodCmdTest {

    @Test
    void execute_periodIsPresent_removePeriod() {
        Update update = new FkUpdate("test", 1L, "20-12-2021").update();
        FkTransaction transaction = new FkTransaction();
        transaction.consume(users -> {
            users.add(1L, "test");
            users.user(1L).periods().add(LocalDate.of(2021, 12, 20));
        });

        Assertions.assertEquals(
            new SendMsg(update, "Есть, мэм!"),
            new RemovePeriodCmd(
                transaction,
                new StringToDateConvert("dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}")
            ).execute(update).orElseGet(() -> new SendMsg(update, "no")));
        Assertions.assertEquals(
            new FkUser(1L, "test",new FkPeriods()),
            transaction.callback(users -> users.user(1L))
        );
    }
}
