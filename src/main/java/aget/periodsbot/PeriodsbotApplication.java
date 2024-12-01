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

package aget.periodsbot;

import aget.periodsbot.bot.PeriodsBot;
import aget.periodsbot.bot.command.CurrentPeriodCmd;
import aget.periodsbot.bot.command.GreetCmd;
import aget.periodsbot.bot.command.KeyboardCmd;
import aget.periodsbot.bot.command.NewPeriodCmd;
import aget.periodsbot.bot.command.PeriodStatisticCmd;
import aget.periodsbot.bot.command.RemovePeriodCmd;
import aget.periodsbot.bot.convert.StringToDateConvert;
import aget.periodsbot.config.BotProps;
import aget.periodsbot.config.PgProps;
import aget.periodsbot.domain.PgTransaction;
import aget.periodsbot.domain.Transaction;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.command.CmdBatch;
import com.github.artemget.teleroute.match.MatchRegex;
import com.github.artemget.teleroute.route.RouteDfs;
import com.github.artemget.teleroute.route.RouteFork;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PeriodsbotApplication {
    public static void main(final String[] args) throws TelegramApiException {
        final Transaction<Users> transaction = new PgTransaction(new PgProps().connectionUrl());
        final String format = "dd.MM";
        new PeriodsBot(
            new BotProps(),
            new RouteDfs<>(
                new RouteFork<>(
                    new MatchRegex<>("/start"),
                    new CmdBatch<>(
                        new GreetCmd(transaction),
                        new KeyboardCmd(
                            "Началось",
                            "Статистика",
                            "Сегодня"
                        )
                    )
                ),
                new RouteFork<>(
                    new MatchRegex<>("Началось"),
                    new NewPeriodCmd(transaction)
                ),
                new RouteFork<>(
                    new MatchRegex<>("Статистика"),
                    new PeriodStatisticCmd(transaction, format)
                ),
                new RouteFork<>(
                    new MatchRegex<>("Сегодня"),
                    new CurrentPeriodCmd(transaction, format)
                ),
                new RouteFork<>(
                    new MatchRegex<>("([Дд]обавить|\\+)\\s*\\d{2}.\\d{2}.\\d{2}"),
                    new NewPeriodCmd(
                        transaction,
                        new StringToDateConvert("dd.MM.yy", "\\d{2}.\\d{2}.\\d{2}")
                    )
                ),
                new RouteFork<>(
                    new MatchRegex<>("([Дд]обавить|\\+)\\s*\\d{2}.\\d{2}"),
                    new NewPeriodCmd(
                        transaction,
                        new StringToDateConvert(
                            new DateTimeFormatterBuilder()
                                .appendPattern(format)
                                .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                                .toFormatter(),
                            "\\d{2}.\\d{2}"
                        )
                    )
                ),
                new RouteFork<>(
                    new MatchRegex<>("([Уу]далить|\\-)\\s*\\d{2}.\\d{2}.\\d{2}"),
                    new RemovePeriodCmd(
                        transaction,
                        new StringToDateConvert("dd.MM.yy", "\\d{2}.\\d{2}.\\d{2}")
                    )
                ),
                new RouteFork<>(
                    new MatchRegex<>("([Уу]далить|\\-)\\s*\\d{2}.\\d{2}"),
                    new RemovePeriodCmd(
                        transaction,
                        new StringToDateConvert(
                            new DateTimeFormatterBuilder()
                                .appendPattern("dd.MM")
                                .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                                .toFormatter(),
                            "\\d{2}.\\d{2}"
                        )
                    )
                ),
                new RouteFork<>(
                    new MatchRegex<>("[Сс]татистика|[Сс]таты \\d+"),
                    new PeriodStatisticCmd(
                        transaction,
                        s -> Integer.parseInt(s.replaceAll("\\D", "")),
                        format
                    )
                )
            )
        ).start();
    }
}
