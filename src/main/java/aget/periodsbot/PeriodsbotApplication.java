package aget.periodsbot;


import aget.periodsbot.bot.PeriodsBot;
import aget.periodsbot.bot.command.*;
import aget.periodsbot.bot.convert.*;
import aget.periodsbot.bot.route.*;
import aget.periodsbot.config.BotProps;
import aget.periodsbot.config.PgProps;
import aget.periodsbot.domain.usecase.CurrPeriod;
import aget.periodsbot.domain.usecase.LastPeriodsStats;
import aget.periodsbot.domain.usecase.PeriodAdd;
import aget.periodsbot.domain.usecase.UserGreet;
import aget.periodsbot.repo.PgUsersFactory;
import aget.periodsbot.repo.UsersFactory;
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
                new InterceptErrorRoute(
                        new BasicRoute(
                                new InterceptErrorRoute(
                                        new TelegramCommandRoute(
                                                new UserGreetCommand(
                                                        "/start",
                                                        new UserGreet(jdbi, usersFactory),
                                                        new UserGreetRqConvert(userTIdConvert),
                                                        new UserGreetRsConvert()
                                                ),
                                                new LastPeriodsStatsCommand(
                                                        "/stats",
                                                        new LastPeriodsStats(jdbi, usersFactory),
                                                        userTIdConvert,
                                                        new PeriodsStatsConvert(lastPeriodStatsConvert)
                                                ),
                                                new CurrPeriodCommand(
                                                        "/current",
                                                        new CurrPeriod(jdbi, usersFactory),
                                                        userTIdConvert,
                                                        lastPeriodStatsConvert
                                                )
                                        ),
                                        new DeadEndRoute(new ErrorCommand())
                                ),
                                new TxtCommandRoute(
                                        new PeriodAddCommand(
                                                "добавить цикл",
                                                new PeriodAdd(jdbi, usersFactory),
                                                new PeriodAddConvert(
                                                        new DateRqConvert("добавить цикл")
                                                ),
                                                new DateRsConvert()
                                        )
                                )
                        )
                )
        ).start();
    }
}
