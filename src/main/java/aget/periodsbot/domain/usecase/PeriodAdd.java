package aget.periodsbot.domain.usecase;

import aget.periodsbot.dto.PeriodAddDto;
import aget.periodsbot.domain.UsersFactory;
import org.jdbi.v3.core.Jdbi;

import java.util.Date;

public class PeriodAdd implements FunctionUseCase<PeriodAddDto, Date> {
    private final Jdbi dataSource;
    private final UsersFactory usersFactory;

    public PeriodAdd(Jdbi dataSource, UsersFactory usersFactory) {
        this.dataSource = dataSource;
        this.usersFactory = usersFactory;
    }

    @Override
    public Date handle(PeriodAddDto periodAddDto) {
        this.dataSource.useTransaction(
            handle ->
                this.usersFactory.provide(handle)
                    .user(periodAddDto.userTelegramIdDto().userTelegramId())
                    .periods()
                    .add(periodAddDto.cycleStartDate())
        );
        return new Date();
    }
}
