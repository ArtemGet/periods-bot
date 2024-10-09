package aget.periodsbot;

import aget.periodsbot.bot.PeriodsBot;
import aget.periodsbot.bot.command.GreetCmd;
import aget.periodsbot.bot.command.NewPeriodCmd;
import aget.periodsbot.bot.command.PeriodStatisticCmd;
import aget.periodsbot.bot.command.RemovePeriodCmd;
import aget.periodsbot.bot.convert.StringToDateConvert;
import aget.periodsbot.config.BotProps;
import aget.periodsbot.config.PgProps;
import aget.periodsbot.context.PgUsersContext;
import aget.periodsbot.context.UsersContext;
import com.github.artemget.teleroute.match.MatchRegex;
import com.github.artemget.teleroute.route.RouteDfs;
import com.github.artemget.teleroute.route.RouteFork;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.format.DateTimeFormatter;

public class PeriodsbotApplication {
    public static void main(String[] args) throws TelegramApiException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        StringToDateConvert stringToDateConvert = new StringToDateConvert(formatter, "\\d{2}-\\d{2}-\\d{4}");

        UsersContext context = new PgUsersContext(new PgProps().connectionUrl());

        new PeriodsBot(
            new BotProps(),
            new RouteDfs<>(
                new RouteFork<>(
                    new MatchRegex<>("/start"),
                    new GreetCmd(context)
                ),
                new RouteFork<>(
                    new MatchRegex<>("([Дд]обавить|\\+) \\d{2}-\\d{2}-\\d{4}"),
                    new NewPeriodCmd(context, stringToDateConvert)
                ),
                new RouteFork<>(
                    new MatchRegex<>("([Уу]далить|\\-) \\d{2}-\\d{2}-\\d{4}"),
                    new RemovePeriodCmd(context, stringToDateConvert)
                ),
                new RouteFork<>(
                    new MatchRegex<>("Статистика|Статы"),
                    new PeriodStatisticCmd(context, formatter)
                )
            )
        ).start();
    }
}
