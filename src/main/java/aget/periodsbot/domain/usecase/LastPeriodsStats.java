package aget.periodsbot.domain.usecase;

import aget.periodsbot.domain.Period;
import aget.periodsbot.domain.Periods;
import aget.periodsbot.dto.LastPeriodStatsDto;
import aget.periodsbot.dto.PeriodStatsDto;
import aget.periodsbot.dto.PeriodsStatsDto;
import aget.periodsbot.dto.UserTIdDto;
import aget.periodsbot.domain.UsersFactory;
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
    private final Integer periodsAmount;
    private static final Integer DEFAULT_PERIOD_AMOUNT = 10;

    public LastPeriodsStats(Jdbi dataSource,
                            UsersFactory usersFactory,
                            Integer periodsAmount) {
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
        Periods.SmartPeriods periods = new Periods.SmartPeriods( this.dataSource.inTransaction(
                handle ->
                        this.usersFactory.provide(handle)
                                .user(input.userTelegramId())
                                .periods()
        ));

        Queue<Period> periodsQ = new ArrayDeque<>(periods.last(this.periodsAmount));
        Collection<PeriodStatsDto> periodStatsDtos = new ArrayList<>();

        while (!periodsQ.isEmpty()) {
            Period period = periodsQ.poll();

            if (periodsQ.isEmpty()) {
                Date currDate = new Date();
                Integer avgPeriodLength = periods.avgLength(DEFAULT_PERIOD_AMOUNT);

                return Optional.of(
                        new PeriodsStatsDto(
                                periodStatsDtos,
                                new LastPeriodStatsDto(
                                        period.start(),
                                        period.days(),
                                        period.days()
                                ),
                                avgPeriodLength)
                );
            } else {
                periodStatsDtos.add(
                        new PeriodStatsDto(
                                period.start()
                        )
                );
            }
        }

        return Optional.empty();
    }
}
