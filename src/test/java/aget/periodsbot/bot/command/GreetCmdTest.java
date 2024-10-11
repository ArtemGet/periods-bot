package aget.periodsbot.bot.command;

import aget.periodsbot.bot.FkUpdate;
import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.fake.FkPeriods;
import aget.periodsbot.domain.fake.FkTransaction;
import aget.periodsbot.domain.fake.FkUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

public class GreetCmdTest {

    @Test
    void execute_userIsNotPresent_addsNew() {
        Update update = new FkUpdate("test", 1L, "text").update();
        FkTransaction transaction = new FkTransaction();

        Assertions.assertEquals(
            new SendMsg(update, "Приветствую, test!"),
            new GreetCmd(transaction)
                .execute(update).orElseGet(() -> new SendMsg(update, "no"))
        );
        Assertions.assertEquals(
            new FkUser(1L, "test", new FkPeriods()),
            transaction.callback(users -> users.user(1L))
        );
    }
}
