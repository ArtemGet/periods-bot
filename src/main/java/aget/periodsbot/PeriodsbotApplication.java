package aget.periodsbot;

import aget.periodsbot.bot.PeriodsBot;
import aget.periodsbot.bot.command.CurrPeriodCommand;
import aget.periodsbot.bot.command.ErrorCommand;
import aget.periodsbot.bot.command.LastPeriodsStatsCommand;
import aget.periodsbot.bot.command.PeriodAddCommand;
import aget.periodsbot.bot.command.UserGreetCommand;
import aget.periodsbot.bot.convert.DateRqConvert;
import aget.periodsbot.bot.convert.DateRsConvert;
import aget.periodsbot.bot.convert.LastPeriodStatsConvert;
import aget.periodsbot.bot.convert.PeriodAddConvert;
import aget.periodsbot.bot.convert.PeriodsStatsConvert;
import aget.periodsbot.bot.convert.UserGreetRqConvert;
import aget.periodsbot.bot.convert.UserGreetRsConvert;
import aget.periodsbot.bot.convert.UserTIdConvert;
import aget.periodsbot.bot.route.BasicRoute;
import aget.periodsbot.bot.route.DeadEndRoute;
import aget.periodsbot.bot.route.InterceptErrorRoute;
import aget.periodsbot.bot.route.TelegramCommandRoute;
import aget.periodsbot.bot.route.TxtCommandRoute;
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
