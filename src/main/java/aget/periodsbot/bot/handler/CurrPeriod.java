package aget.periodsbot.bot.handler;

import aget.periodsbot.domain.Period;
import aget.periodsbot.dto.UserTIdDto;
import aget.periodsbot.repo.Periods;
import aget.periodsbot.repo.UsersFactory;
import org.jdbi.v3.core.Jdbi;

import java.util.Date;

public class CurrPeriod implements FunctionUseCase<UserTIdDto, String> {
    private final Jdbi dataSource;
    private final UsersFactory usersFactory;

    public CurrPeriod(Jdbi dataSource, UsersFactory usersFactory) {
        this.dataSource = dataSource;
        this.usersFactory = usersFactory;
    }


    @Override
    public String handle(UserTIdDto input) {
        Periods periods = this.dataSource.inTransaction(handle -> this.usersFactory.provide(handle)
                .user(input.userTelegramId())
                .periods());
        Period currPeriod = periods.getCurrentPeriod();

        Date currDate = new Date();
        return String.format("Начало цикла: %s. С начала цикла прошло %s дней, До начала следующего цикла предположительно %s дней",
                currPeriod.cycleStartDate(),
                currPeriod.daysPassedFromCycleStart(currDate),
                currPeriod.predictDaysBeforeCycleEnd(currDate, periods.averagePeriodLength()));
    }
}
