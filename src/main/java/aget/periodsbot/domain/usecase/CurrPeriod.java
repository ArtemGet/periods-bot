package aget.periodsbot.domain.usecase;

import aget.periodsbot.domain.Period;
import aget.periodsbot.dto.LastPeriodStatsDto;
import aget.periodsbot.dto.UserTIdDto;
import aget.periodsbot.repo.Periods;
import aget.periodsbot.repo.UsersFactory;
import org.jdbi.v3.core.Jdbi;

import java.util.Date;

public class CurrPeriod implements FunctionUseCase<UserTIdDto, LastPeriodStatsDto> {
    private final Jdbi dataSource;
    private final UsersFactory usersFactory;

    public CurrPeriod(Jdbi dataSource, UsersFactory usersFactory) {
        this.dataSource = dataSource;
        this.usersFactory = usersFactory;
    }

    @Override
    public LastPeriodStatsDto handle(UserTIdDto input) {
        Periods periods = this.dataSource.inTransaction(
                handle -> this.usersFactory.provide(handle)
                        .user(input.userTelegramId())
                        .periods()
        );

        Period currPeriod = periods.currentPeriod();
        Date currDate = new Date();

        return new LastPeriodStatsDto(
                currPeriod.cycleStartDate(),
                currPeriod.daysPassedFromCycleStart(currDate),
                currPeriod.predictDaysBeforeCycleEnd(currDate, periods.avgPeriodLength())
        );
    }
}
