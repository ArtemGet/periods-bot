package aget.periodsbot.bot.handler;

import aget.periodsbot.domain.Period;
import aget.periodsbot.dto.UserTIdDto;
import aget.periodsbot.repo.Periods;
import aget.periodsbot.repo.UsersFactory;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

public class LastPeriodsStats implements FunctionUseCase<UserTIdDto, String> {
    private final Jdbi ds;
    private final UsersFactory usersFactory;
    private final Long periodsAmount;
    private static final Long DEFAULT_PERIOD_AMOUNT = 10L;

    public LastPeriodsStats(Jdbi ds, UsersFactory usersFactory, Long periodsAmount) {
        this.ds = ds;
        this.usersFactory = usersFactory;
        this.periodsAmount = periodsAmount;
    }

    public LastPeriodsStats(Jdbi ds, UsersFactory usersFactory) {
        this.ds = ds;
        this.usersFactory = usersFactory;
        this.periodsAmount = DEFAULT_PERIOD_AMOUNT;
    }

    @Override
    public String handle(UserTIdDto input) {
        Periods periods = this.ds.inTransaction(ha ->
                this.usersFactory.provide(ha)
                        .user(input.userTelegramId())
                        .periods());

        Queue<Period> periodsQ = new ArrayDeque<>(periods.lastPeriods(this.periodsAmount));
        StringBuilder builder = new StringBuilder();

        while (periodsQ.iterator().hasNext()) {
            Period period = periodsQ.poll();

            if (periodsQ.iterator().hasNext()) {
                Period nextPeriod = periodsQ.poll();

                Date periodEndDate = period.cycleEndDate(nextPeriod);

                builder.append(String.format("Начало цикла: %s, конец цикла: %s",
                                period.cycleStartDate(),
                                periodEndDate))
                        .append("\n");
            } else {
                Date currDate = new Date();
                Integer avgPeriodLength = periods.averagePeriodLength();
                builder.append(String.format("Начало цикла: %s. С начала цикла прошло %s дней, До начала следующего цикла предположительно %s дней",
                                period.cycleStartDate(),
                                period.daysPassedFromCycleStart(currDate),
                                period.predictDaysBeforeCycleEnd(currDate, avgPeriodLength)))
                        .append("\n")
                        .append(String.format("Средняя продолжительность цикла: %s", avgPeriodLength));
            }
        }

        return builder.toString();
    }
}
