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

import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.Transaction;
import aget.periodsbot.domain.Period;
import aget.periodsbot.domain.Periods;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CurrentPeriodCmd implements Cmd<Update, AbsSender> {
    private final Transaction<Users> transaction;
    private final DateTimeFormatter formatter;

    public CurrentPeriodCmd(Transaction<Users> transaction, String format) {
        this(transaction, DateTimeFormatter.ofPattern(format));
    }

    public CurrentPeriodCmd(Transaction<Users> transaction, DateTimeFormatter formatter) {
        this.transaction = transaction;
        this.formatter = formatter;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        return Optional.of(
            new SendMsg(
                update,
                transaction.callback(
                    users -> {
                        Periods.SmartPeriods smartPeriods = new Periods.SmartPeriods(
                            users.user(update.getMessage().getFrom().getId()).periods()
                        );
                        Period current = smartPeriods.current();
                        return String.format("Началось: %s\nДень: %s\nОсталось: %s",
                            current.start().format(formatter),
                            current.days().toString(),
                            smartPeriods.avgLength(10) - current.days());
                    }
                )
            )
        );
    }
}
