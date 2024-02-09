package aget.periodsbot.bot.handler;

import aget.periodsbot.dto.UserGreetDto;
import aget.periodsbot.repo.UsersFactory;
import org.jdbi.v3.core.Jdbi;

public class UserGreet implements FunctionUseCase<UserGreetDto, String> {
    private final Jdbi ds;
    private final UsersFactory usersFactory;

    public UserGreet(Jdbi ds, UsersFactory usersFactory) {
        this.ds = ds;
        this.usersFactory = usersFactory;
    }

    @Override
    public String handle(UserGreetDto userTIdDto) {
        return String.format("Добро пожаловать %s, снова",
                this.ds.inTransaction(ha ->
                                this.usersFactory.provide(ha)
                                        .add(userTIdDto.userTelegramIdDto().userTelegramId(), userTIdDto.name()))
                        .name());
    }
}
