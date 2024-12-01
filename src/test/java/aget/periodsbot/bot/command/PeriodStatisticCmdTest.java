/*
 * MIT License
 *
 * Copyright (c) 2024 Artem Getmanskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
