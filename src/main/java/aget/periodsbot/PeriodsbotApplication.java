package aget.periodsbot;


import aget.periodsbot.bot.PeriodsBot;
import aget.periodsbot.bot.command.ErrorCommand;
import aget.periodsbot.bot.command.LastPeriodsStatsCommand;
import aget.periodsbot.bot.command.PeriodAddCommand;
import aget.periodsbot.bot.command.UserGreetCommand;
import aget.periodsbot.bot.convert.DateReqConvert;
import aget.periodsbot.bot.convert.LastPeriodsStatsReqConvert;
import aget.periodsbot.bot.convert.LastPeriodsStatsRespConvert;
import aget.periodsbot.bot.convert.PeriodAddReqDtoConvert;
import aget.periodsbot.bot.convert.PeriodAddRespConvert;
import aget.periodsbot.bot.convert.UserGreetReqConvert;
import aget.periodsbot.bot.convert.UserGreetRespConvert;
import aget.periodsbot.bot.route.BasicRoute;
import aget.periodsbot.bot.route.DeadEndRoute;
import aget.periodsbot.bot.route.InterceptErrorRoute;
import aget.periodsbot.bot.route.TelegramCommandRoute;
import aget.periodsbot.bot.route.TextCommandRoute;
import aget.periodsbot.config.BotProps;
import aget.periodsbot.config.PgProps;
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

        new PeriodsBot(
                new BotProps(),
                new InterceptErrorRoute(
                        new BasicRoute(
                                new InterceptErrorRoute(
                                        new TelegramCommandRoute(
                                                new UserGreetCommand(
                                                        "/start",
                                                        new UserGreet(jdbi, usersFactory),
                                                        new UserGreetReqConvert(),
                                                        new UserGreetRespConvert()
                                                ),
                                                new LastPeriodsStatsCommand(
                                                        "/stats",
                                                        new LastPeriodsStats(jdbi, usersFactory),
                                                        new LastPeriodsStatsReqConvert(),
                                                        new LastPeriodsStatsRespConvert()
                                                )
                                        ),
                                        new DeadEndRoute(new ErrorCommand())
                                ),
                                new TextCommandRoute(
                                        new PeriodAddCommand(
                                                "добавить цикл",
                                                new PeriodAdd(jdbi, usersFactory),
                                                new PeriodAddReqDtoConvert(
                                                        new DateReqConvert("добавить цикл")
                                                ),
                                                new PeriodAddRespConvert()
                                        )
                                )
                        )
                )
        ).start();
    }
}
