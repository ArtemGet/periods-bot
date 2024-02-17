package aget.periodsbot.domain.usecase;

import aget.periodsbot.domain.Period;
import aget.periodsbot.dto.LastPeriodStatsDto;
import aget.periodsbot.dto.PeriodStatsDto;
import aget.periodsbot.dto.PeriodsStatsDto;
import aget.periodsbot.dto.UserTIdDto;
import aget.periodsbot.repo.Periods;
import aget.periodsbot.repo.UsersFactory;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Queue;

public class LastPeriodsStats implements FunctionUseCase<UserTIdDto, Optional<PeriodsStatsDto>> {
    private final Jdbi dataSource;
    private final UsersFactory usersFactory;
    private final Long periodsAmount;
    private static final Long DEFAULT_PERIOD_AMOUNT = 10L;

    public LastPeriodsStats(Jdbi dataSource,
                            UsersFactory usersFactory,
                            Long periodsAmount) {
        this.dataSource = dataSource;
        this.usersFactory = usersFactory;
        this.periodsAmount = periodsAmount;
    }

    public LastPeriodsStats(Jdbi dataSource,
                            UsersFactory usersFactory) {
        this.dataSource = dataSource;
        this.usersFactory = usersFactory;
        this.periodsAmount = DEFAULT_PERIOD_AMOUNT;
    }

    @Override
    public Optional<PeriodsStatsDto> handle(UserTIdDto input) {
        Periods periods = this.dataSource.inTransaction(
                handle ->
                        this.usersFactory.provide(handle)
                                .user(input.userTelegramId())
                                .periods()
        );

        Queue<Period> periodsQ = new ArrayDeque<>(periods.lastPeriods(this.periodsAmount));
        Collection<PeriodStatsDto> periodStatsDtos = new ArrayList<>();

        while (!periodsQ.isEmpty()) {
            Period period = periodsQ.poll();

            if (periodsQ.isEmpty()) {
                Date currDate = new Date();
                Integer avgPeriodLength = periods.avgPeriodLength();

                return Optional.of(
                        new PeriodsStatsDto(
                                periodStatsDtos,
                                new LastPeriodStatsDto(
                                        period.cycleStartDate(),
                                        period.daysPassedFromCycleStart(currDate),
                                        period.predictDaysBeforeCycleEnd(currDate, avgPeriodLength)
                                ),
                                avgPeriodLength)
                );
            } else {
                periodStatsDtos.add(
                        new PeriodStatsDto(
                                period.cycleStartDate(),
                                period.cycleEndDate(periodsQ.peek())
                        )
                );
            }
        }

        return Optional.empty();
    }
}
