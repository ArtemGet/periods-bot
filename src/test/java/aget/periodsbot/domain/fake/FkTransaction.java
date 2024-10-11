package aget.periodsbot.domain.fake;

import aget.periodsbot.domain.Transaction;
import aget.periodsbot.domain.Users;

import java.util.function.Consumer;
import java.util.function.Function;

public class FkTransaction implements Transaction<Users> {

    private final Users users;

    public FkTransaction() {
        this(new FkUsers());
    }

    public FkTransaction(Users users) {
        this.users = users;
    }

    @Override
    public <R> R callback(Function<Users, R> fn) {
        return fn.apply(users);
    }

    @Override
    public void consume(Consumer<Users> fn) {
        fn.accept(users);
    }
}
