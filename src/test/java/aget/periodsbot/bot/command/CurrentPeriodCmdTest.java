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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Test case for {@link CurrentPeriodCmd}.
 *
 * @since 0.1.0
 */
final class CurrentPeriodCmdTest {
    @Test
    void shouldReturnCurrentPeriod() {
        final Update update = new FkUpdate().update();
        final LocalDate last = LocalDate.now();
        final String format = "dd.MM.yyyy";
        final FkTransaction transaction = new FkTransaction();
        transaction.consume(
            users -> {
                users.add(1L, "test");
                users.user(1L).periods().add(last.minusDays(5));
                users.user(1L).periods().add(last.minusDays(35));
                users.user(1L).periods().add(last.minusDays(65));
            });
        Assertions.assertEquals(
            new SendMsg(
                update,
                String.format(
                    "Началось: %s\nДень: %s\nОсталось: %s",
                    last.minusDays(5).format(DateTimeFormatter.ofPattern(format)),
                    6,
                    25
                )
            ),
            new CurrentPeriodCmd(
                transaction,
                format
            ).execute(update).orElseGet(() -> new SendMsg(update, "no"))
        );
    }
}
