package aget.periodsbot;

import aget.periodsbot.bot.PeriodsBot;
import aget.periodsbot.bot.command.CurrPeriodCmd;
import aget.periodsbot.bot.command.ErrorCmd;
import aget.periodsbot.bot.command.LastPeriodsStatsCmd;
import aget.periodsbot.bot.command.PeriodAddCmd;
import aget.periodsbot.bot.command.UserGreetCmd;
import aget.periodsbot.bot.command.trigger.TriggerCmd;
import aget.periodsbot.bot.convert.DateRqConvert;
import aget.periodsbot.bot.convert.DateRsConvert;
import aget.periodsbot.bot.convert.LastPeriodStatsConvert;
import aget.periodsbot.bot.convert.PeriodAddConvert;
import aget.periodsbot.bot.convert.PeriodsStatsConvert;
import aget.periodsbot.bot.convert.UserGreetRqConvert;
import aget.periodsbot.bot.convert.UserGreetRsConvert;
import aget.periodsbot.bot.convert.UserTIdConvert;
import aget.periodsbot.bot.route.EndRoute;
import aget.periodsbot.bot.route.InterceptErrRoute;
import aget.periodsbot.bot.route.IterateRoute;
import aget.periodsbot.bot.route.SecureRoute;
import aget.periodsbot.bot.route.TgCmdRoute;
import aget.periodsbot.bot.route.TxtCmdRoute;
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
                new SecureRoute(
                        new InterceptErrRoute(
                                new IterateRoute(
                                        new InterceptErrRoute(
                                                new TgCmdRoute(
                                                        new IterateRoute(
                                                                new InterceptErrRoute(
                                                                        new TxtCmdRoute(
                                                                                new TriggerCmd<>(
                                                                                        "/start",
                                                                                        new UserGreetCmd(
                                                                                                new UserGreet(jdbi, usersFactory),
                                                                                                new UserGreetRqConvert(userTIdConvert),
                                                                                                new UserGreetRsConvert()
                                                                                        )
                                                                                )
                                                                        ),
                                                                        new EndRoute()
                                                                ),
                                                                new TxtCmdRoute(
                                                                        new TriggerCmd<>(
                                                                                "/stats",
                                                                                new LastPeriodsStatsCmd(
                                                                                        new LastPeriodsStats(jdbi, usersFactory),
                                                                                        userTIdConvert,
                                                                                        new PeriodsStatsConvert(lastPeriodStatsConvert)
                                                                                )
                                                                        ),
                                                                        new TriggerCmd<>(
                                                                                "/current",
                                                                                new CurrPeriodCmd(
                                                                                        new CurrPeriod(jdbi, usersFactory),
                                                                                        userTIdConvert,
                                                                                        lastPeriodStatsConvert
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new EndRoute(new ErrorCmd())
                                        ),
                                        new TxtCmdRoute(
                                                new TriggerCmd<>(
                                                        "добавить цикл",
                                                        new PeriodAddCmd(
                                                                new PeriodAdd(jdbi, usersFactory),
                                                                new PeriodAddConvert(
                                                                        new DateRqConvert("добавить цикл")
                                                                ),
                                                                new DateRsConvert()
                                                        )
                                                )
                                        )
                                ),
                                new EndRoute()
                        )
                )
        ).start();
    }
}
