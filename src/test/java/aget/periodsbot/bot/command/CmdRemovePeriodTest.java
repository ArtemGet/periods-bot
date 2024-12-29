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
import aget.periodsbot.bot.convert.StringToDateConvert;
import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.fake.FkPeriods;
import aget.periodsbot.domain.fake.FkTransaction;
import aget.periodsbot.domain.fake.FkUser;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Test case for {@link CmdRemovePeriod}.
 *
 * @since 0.1.0
 */
final class CmdRemovePeriodTest {
    @Test
    void shouldRemovePeriod() {
        final Update update = new FkUpdate("test", 1L, "20-12-2021").update();
        final FkTransaction transaction = new FkTransaction();
        transaction.consume(
            users -> {
                users.add(1L, "test");
                users.user(1L).periods().add(LocalDate.of(2021, 12, 20));
            });
        Assertions.assertEquals(
            new SendMsg(update, "Есть, мэм!"),
            new CmdRemovePeriod(
                transaction,
                new StringToDateConvert("dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}")
            ).execute(update).orElseGet(() -> new SendMsg(update, "no"))
        );
        Assertions.assertEquals(
            new FkUser(1L, "test", new FkPeriods()),
            transaction.callback(users -> users.user(1L))
        );
    }
}
