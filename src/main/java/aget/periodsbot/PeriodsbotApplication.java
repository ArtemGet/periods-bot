package aget.periodsbot;


import aget.periodsbot.bot.PeriodsBot;
import aget.periodsbot.bot.command.NoSuchCommand;
import aget.periodsbot.bot.command.PeriodAddCommand;
import aget.periodsbot.bot.command.UserGreetCommand;
import aget.periodsbot.bot.convert.DateReqConvert;
import aget.periodsbot.bot.convert.PeriodAddReqDtoConvert;
import aget.periodsbot.bot.convert.PeriodAddRespConvert;
import aget.periodsbot.bot.convert.UserGreetReqConvert;
import aget.periodsbot.bot.convert.UserGreetRespConvert;
import aget.periodsbot.bot.route.BasicRoute;
import aget.periodsbot.bot.route.DeadEndRoute;
import aget.periodsbot.bot.route.InterceptErrorRoute;
import aget.periodsbot.bot.route.TCommandRoute;
import aget.periodsbot.bot.route.TextRoute;
import aget.periodsbot.config.BotProps;
import aget.periodsbot.config.PgProps;
import aget.periodsbot.domain.usecase.PeriodAdd;
import aget.periodsbot.domain.usecase.UserGreet;
import aget.periodsbot.repo.PgUsersFactory;
import org.jdbi.v3.core.Jdbi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PeriodsbotApplication {

    public static void main(String[] args) throws TelegramApiException {
        Jdbi jdbi = Jdbi.create(new PgProps().connectionUrl());

        new PeriodsBot(
                new BotProps(),
                new InterceptErrorRoute(
                        new BasicRoute(
                                new InterceptErrorRoute(
                                        new TCommandRoute(new UserGreetCommand("/start",
                                                new UserGreet(jdbi,
                                                        new PgUsersFactory()),
                                                new UserGreetReqConvert(),
                                                new UserGreetRespConvert())),
                                        new DeadEndRoute(new NoSuchCommand())
                                ),
                                new TextRoute(
                                        new PeriodAddCommand("добавить цикл",
                                                new PeriodAdd(jdbi,
                                                        new PgUsersFactory()),
                                                new PeriodAddReqDtoConvert(
                                                        new DateReqConvert("добавить цикл")
                                                ),
                                                new PeriodAddRespConvert())))))
                .start();
    }

}
