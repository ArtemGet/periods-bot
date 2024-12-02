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
import aget.periodsbot.domain.Periods;
import aget.periodsbot.domain.Transaction;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Command show user's last periods.
 *
 * @since 0.1.0
 */
public final class PeriodStatisticCmd implements Cmd<Update, AbsSender> {
    /**
     * Transaction.
     */
    private final Transaction<Users> transaction;

    /**
     * Convert count entered by user to integer.
     */
    private final Function<String, Integer> count;

    /**
     * Date display format.
     */
    private final DateTimeFormatter formatter;

    public PeriodStatisticCmd(
        final Transaction<Users> transaction,
        final String format
    ) {
        this(transaction, 5, format);
    }

    public PeriodStatisticCmd(
        final Transaction<Users> transaction,
        final Integer count,
        final String format
    ) {
        this(transaction, s -> count, DateTimeFormatter.ofPattern(format));
    }

    public PeriodStatisticCmd(
        final Transaction<Users> transaction,
        final Function<String, Integer> count,
        final String format
    ) {
        this(transaction, count, DateTimeFormatter.ofPattern(format));
    }

    public PeriodStatisticCmd(
        final Transaction<Users> transaction,
        final Function<String, Integer> count,
        final DateTimeFormatter formatter
    ) {
        this.transaction = transaction;
        this.count = count;
        this.formatter = formatter;
    }

    @Override
    public Optional<Send<AbsSender>> execute(final Update update) {
        return Optional.of(
            new SendMsg(
                update,
                this.transaction.callback(
                    users ->
                        new Periods.SmartPeriods(
                            users.user(update.getMessage().getFrom().getId()).periods()
                        ).last(this.count.apply(update.getMessage().getText()))
                            .stream()
                            .map(
                                p -> String.format(
                                    "%s - %s",
                                    p.start().format(this.formatter),
                                    p.days().toString()
                                ))
                            .collect(Collectors.joining("\n"))
                )
            )
        );
    }
}
