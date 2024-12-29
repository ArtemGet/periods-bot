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
import aget.periodsbot.bot.command.CmdCurrentPeriod;
import aget.periodsbot.bot.command.CmdGreet;
import aget.periodsbot.bot.command.CmdKeyboard;
import aget.periodsbot.bot.command.CmdNewPeriod;
import aget.periodsbot.bot.command.CmdPeriodStatistic;
import aget.periodsbot.bot.command.CmdRemovePeriod;
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
    private static final String DATE_FORMAT = "dd.MM.yy";
    private static final String SHORT_DATE_FORMAT = "dd.MM";

    private static final String DATE_PATTERN = "\\d{2}.\\d{2}.\\d{2}";
    private static final String SHORT_DATE_PATTERN = "\\d{2}.\\d{2}";

    public static void main(final String[] args) throws TelegramApiException {
        final Transaction<Users> transaction = new PgTransaction(new PgProps().connectionUrl());
        new PeriodsBot(
            new BotProps(),
            new RouteDfs<>(
                new RouteFork<>(
                    new MatchRegex<>("/start"),
                    new CmdBatch<>(
                        new CmdGreet(transaction),
                        new CmdKeyboard(
                            "Началось",
                            "Статистика",
                            "Сегодня"
                        )
                    )
                ),
                new RouteFork<>(
                    new MatchRegex<>("Началось"),
                    new CmdNewPeriod(transaction)
                ),
                new RouteFork<>(
                    new MatchRegex<>("Статистика"),
                    new CmdPeriodStatistic(transaction, SHORT_DATE_FORMAT)
                ),
                new RouteFork<>(
                    new MatchRegex<>("Сегодня"),
                    new CmdCurrentPeriod(transaction, SHORT_DATE_FORMAT)
                ),
                new RouteFork<>(
                    new MatchRegex<>("([Дд]обавить|\\+)\\s*"),
                    new RouteDfs<>(
                        new RouteFork<>(
                            new MatchRegex<>(DATE_PATTERN),
                            new CmdNewPeriod(
                                transaction,
                                new StringToDateConvert(DATE_FORMAT, DATE_PATTERN)
                            )
                        ),
                        new RouteFork<>(
                            new MatchRegex<>(SHORT_DATE_PATTERN),
                            new CmdNewPeriod(
                                transaction,
                                new StringToDateConvert(
                                    new DateTimeFormatterBuilder()
                                        .appendPattern(SHORT_DATE_FORMAT)
                                        .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                                        .toFormatter(),
                                    SHORT_DATE_PATTERN
                                )
                            )
                        )
                    )
                ),
                new RouteFork<>(
                    new MatchRegex<>("([Уу]далить|\\-)\\s*"),
                    new RouteDfs<>(
                        new RouteFork<>(
                            new MatchRegex<>(DATE_PATTERN),
                            new CmdRemovePeriod(
                                transaction,
                                new StringToDateConvert(DATE_FORMAT, DATE_PATTERN)
                            )
                        ),
                        new RouteFork<>(
                            new MatchRegex<>(SHORT_DATE_PATTERN),
                            new CmdRemovePeriod(
                                transaction,
                                new StringToDateConvert(
                                    new DateTimeFormatterBuilder()
                                        .appendPattern(SHORT_DATE_FORMAT)
                                        .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                                        .toFormatter(),
                                    SHORT_DATE_PATTERN
                                )
                            )
                        )
                    )
                ),
                new RouteFork<>(
                    new MatchRegex<>("[Сс]татистика|[Сс]таты \\d+"),
                    new CmdPeriodStatistic(
                        transaction,
                        s -> Integer.parseInt(s.replaceAll("\\D", "")),
                        SHORT_DATE_FORMAT
                    )
                )
            )
        ).start();
    }
}
