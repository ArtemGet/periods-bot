package aget.periodsbot;

import aget.periodsbot.bot.PeriodsBot;
import aget.periodsbot.bot.command.CurrPeriodCmd;
import aget.periodsbot.bot.command.LastPeriodsStatsCmd;
import aget.periodsbot.bot.command.PeriodAddCmd;
import aget.periodsbot.bot.command.UserGreetCmd;
import aget.periodsbot.bot.convert.DateRqConvert;
import aget.periodsbot.bot.convert.DateRsConvert;
import aget.periodsbot.bot.convert.LastPeriodStatsConvert;
import aget.periodsbot.bot.convert.PeriodAddConvert;
import aget.periodsbot.bot.convert.PeriodsStatsConvert;
import aget.periodsbot.bot.convert.UserGreetRqConvert;
import aget.periodsbot.bot.convert.UserGreetRsConvert;
import aget.periodsbot.bot.convert.UserTIdConvert;
import aget.periodsbot.config.BotProps;
import aget.periodsbot.config.PgProps;
import aget.periodsbot.domain.usecase.CurrPeriod;
import aget.periodsbot.domain.usecase.LastPeriodsStats;
import aget.periodsbot.domain.usecase.PeriodAdd;
import aget.periodsbot.domain.usecase.UserGreet;
import aget.periodsbot.repo.PgUsersFactory;
import aget.periodsbot.repo.UsersFactory;
import com.github.artemget.teleroute.match.MatchTextExact;
import com.github.artemget.teleroute.route.RouteDfs;
import com.github.artemget.teleroute.route.RouteFork;
import org.jdbi.v3.core.Jdbi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PeriodsbotApplication {
    public static void main(String[] args) throws TelegramApiException {
        Jdbi jdbi = Jdbi.create(new PgProps().connectionUrl());
        UsersFactory usersFactory = new PgUsersFactory();

        UserTIdConvert userTIdConvert = new UserTIdConvert();
        LastPeriodStatsConvert lastPeriodStatsConvert = new LastPeriodStatsConvert();

        new PeriodsBot(
            new BotProps(),
            new RouteDfs<>(
                new RouteFork<>(
                    new MatchTextExact<>("/start"),
                    new UserGreetCmd(
                        new UserGreet(jdbi, usersFactory),
                        new UserGreetRqConvert(userTIdConvert),
                        new UserGreetRsConvert()
                    )
                ),
                new RouteFork<>(
                    new MatchTextExact<>("/stats"),
                    new LastPeriodsStatsCmd(
                        new LastPeriodsStats(jdbi, usersFactory),
                        userTIdConvert,
                        new PeriodsStatsConvert(lastPeriodStatsConvert)
                    )
                ),
                new RouteFork<>(
                    new MatchTextExact<>("/current"),
                    new CurrPeriodCmd(
                        new CurrPeriod(jdbi, usersFactory),
                        userTIdConvert,
                        lastPeriodStatsConvert
                    )
                ),
                new RouteFork<>(
                    new MatchTextExact<>("добавить"),
                    new PeriodAddCmd(
                        new PeriodAdd(jdbi, usersFactory),
                        new PeriodAddConvert(
                            new DateRqConvert("добавить")
                        ),
                        new DateRsConvert()
                    )
                )
            )
        ).start();
    }
}
