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
import aget.periodsbot.domain.Period;
import aget.periodsbot.domain.Periods;
import aget.periodsbot.domain.Transaction;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Command shows user's current period.
 *
 * @since 0.1.0
 */
public final class CmdCurrentPeriod implements Cmd<Update, AbsSender> {
    /**
     * Transaction.
     */
    private final Transaction<Users> transaction;

    /**
     * Date display format.
     */
    private final DateTimeFormatter formatter;

    public CmdCurrentPeriod(final Transaction<Users> transaction, final String format) {
        this(transaction, DateTimeFormatter.ofPattern(format));
    }

    public CmdCurrentPeriod(
        final Transaction<Users> transaction,
        final DateTimeFormatter formatter
    ) {
        this.transaction = transaction;
        this.formatter = formatter;
    }

    @Override
    public Optional<Send<AbsSender>> execute(final Update update) {
        return Optional.of(
            new SendMsg(
                update,
                this.transaction.callback(
                    users -> {
                        final Periods.SmartPeriods periods = new Periods.SmartPeriods(
                            users.user(update.getMessage().getFrom().getId()).periods()
                        );
                        final Period current = periods.current();
                        return String.format(
                            "Началось: %s\nДень: %s\nОсталось: %s",
                            current.start().format(this.formatter),
                            current.days().toString(),
                            periods.avgLength(10) - current.days()
                        );
                    }
                )
            )
        );
    }
}
