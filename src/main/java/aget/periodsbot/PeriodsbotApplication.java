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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class PeriodsbotApplication {
    public static void main(String[] args) throws TelegramApiException {
        Transaction<Users> transaction = new PgTransaction(new PgProps().connectionUrl());

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
                    new PeriodStatisticCmd(transaction, "dd.MM")
                ),
                new RouteFork<>(
                    new MatchRegex<>("Сегодня"),
                    new CurrentPeriodCmd(transaction,"dd.MM")
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
                                .appendPattern("dd.MM")
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
                        "dd.MM")
                )
            )
        ).start();
    }
}
