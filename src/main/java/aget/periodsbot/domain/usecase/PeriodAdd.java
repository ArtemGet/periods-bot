package aget.periodsbot.domain.usecase;

import aget.periodsbot.dto.PeriodAddDto;
import aget.periodsbot.repo.UsersFactory;
import org.jdbi.v3.core.Jdbi;

import java.util.Date;

public class PeriodAdd implements FunctionUseCase<PeriodAddDto, Date> {
    private final Jdbi ds;
    private final UsersFactory usersFactory;

    public PeriodAdd(Jdbi ds, UsersFactory usersFactory) {
        this.ds = ds;
        this.usersFactory = usersFactory;
    }

    @Override
    public Date handle(PeriodAddDto periodAddDto) {
        return this.ds.inTransaction(ha -> this.usersFactory.provide(ha)
                        .user(periodAddDto.userTelegramIdDto().userTelegramId())
                        .periods()
                        .add(periodAddDto.cycleStartDate()))
                .cycleStartDate();
    }
}
