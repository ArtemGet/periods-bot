package aget.periodsbot.bot.handler;

import aget.periodsbot.dto.PeriodAddDto;
import aget.periodsbot.repo.UsersFactory;
import org.jdbi.v3.core.Jdbi;

public class PeriodAdd implements FunctionUseCase<PeriodAddDto, String> {
    private final Jdbi ds;
    private final UsersFactory usersFactory;

    public PeriodAdd(Jdbi ds, UsersFactory usersFactory) {
        this.ds = ds;
        this.usersFactory = usersFactory;
    }

    @Override
    public String handle(PeriodAddDto periodAddDto) {
        return String.format("Добавлено начало цикла: %s",
                this.ds.inTransaction(ha -> this.usersFactory.provide(ha)
                                .user(periodAddDto.userTelegramIdDto().userTelegramId())
                                .periods()
                                .add(periodAddDto.cycleStartDate()))
                        .cycleStartDate());
    }
}
