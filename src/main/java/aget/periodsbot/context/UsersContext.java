package aget.periodsbot.context;

import aget.periodsbot.domain.Users;

import java.util.function.Consumer;
import java.util.function.Function;

public interface UsersContext {
    <R> R callback(Function<Users, R> fn);

    void consume(Consumer<Users> fn);
}
