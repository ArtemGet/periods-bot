package aget.periodsbot;

import aget.periodsbot.bot.PeriodsBot;
import aget.periodsbot.bot.command.GreetCmd;
import aget.periodsbot.bot.command.NewPeriodCmd;
import aget.periodsbot.bot.command.PeriodStatisticCmd;
import aget.periodsbot.bot.command.RemovePeriodCmd;
import aget.periodsbot.bot.convert.StringToDateConvert;
import aget.periodsbot.config.BotProps;
import aget.periodsbot.config.PgProps;
import aget.periodsbot.context.PgTransaction;
import aget.periodsbot.context.Transaction;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.match.MatchRegex;
import com.github.artemget.teleroute.route.RouteDfs;
import com.github.artemget.teleroute.route.RouteFork;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PeriodsbotApplication {
    public static void main(String[] args) throws TelegramApiException {
        String dateFormat = "dd-MM-yyyy";
        StringToDateConvert stringToDateConvert = new StringToDateConvert(dateFormat, "\\d{2}-\\d{2}-\\d{4}");

        Transaction<Users> transaction = new PgTransaction(new PgProps().connectionUrl());

        new PeriodsBot(
            new BotProps(),
            new RouteDfs<>(
                new RouteFork<>(
                    new MatchRegex<>("/start"),
                    new GreetCmd(transaction)
                ),
                new RouteFork<>(
                    new MatchRegex<>("([Дд]обавить|\\+) \\d{2}-\\d{2}-\\d{4}"),
                    new NewPeriodCmd(transaction, stringToDateConvert)
                ),
                new RouteFork<>(
                    new MatchRegex<>("([Уу]далить|\\-) \\d{2}-\\d{2}-\\d{4}"),
                    new RemovePeriodCmd(transaction, stringToDateConvert)
                ),
                new RouteFork<>(
                    new MatchRegex<>("Статистика|Статы"),
                    new PeriodStatisticCmd(transaction, "dd.MM")

                )
            )
        ).start();
    }
}
