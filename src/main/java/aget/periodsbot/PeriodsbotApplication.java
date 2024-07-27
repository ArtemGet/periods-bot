package aget.periodsbot;

import aget.periodsbot.bot.PeriodsBot;
import aget.periodsbot.bot.command.GreetCmd;
import aget.periodsbot.config.BotProps;
import aget.periodsbot.config.PgProps;
import aget.periodsbot.context.PgUsersContext;
import aget.periodsbot.context.UsersContext;
import com.github.artemget.teleroute.match.MatchTextExact;
import com.github.artemget.teleroute.route.RouteDfs;
import com.github.artemget.teleroute.route.RouteFork;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PeriodsbotApplication {
    public static void main(String[] args) throws TelegramApiException {
        UsersContext context = new PgUsersContext(new PgProps().connectionUrl());

        new PeriodsBot(
            new BotProps(),
            new RouteDfs<>(
                new RouteFork<>(
                    new MatchTextExact<>("/start"),
                    new GreetCmd(context)
                )
            )
        ).start();
    }
}
