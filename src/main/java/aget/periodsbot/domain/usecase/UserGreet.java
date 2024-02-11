package aget.periodsbot.domain.usecase;

import aget.periodsbot.dto.UserGreetRqDto;
import aget.periodsbot.repo.UsersFactory;
import org.jdbi.v3.core.Jdbi;

public class UserGreet implements FunctionUseCase<UserGreetRqDto, String> {
    private final Jdbi dataSource;
    private final UsersFactory usersFactory;

    public UserGreet(Jdbi dataSource, UsersFactory usersFactory) {
        this.dataSource = dataSource;
        this.usersFactory = usersFactory;
    }

    @Override
    public String handle(UserGreetRqDto telegramUser) {
        return this.dataSource.inTransaction(handle ->
                        this.usersFactory.provide(handle)
                                .add(telegramUser.userTelegramIdDto().userTelegramId(),
                                        telegramUser.name()))
                .name();
    }
}
