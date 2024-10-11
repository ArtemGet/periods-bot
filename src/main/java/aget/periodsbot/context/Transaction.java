package aget.periodsbot.context;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Transaction<D> {
    <R> R callback(Function<D, R> fn);

    void consume(Consumer<D> fn);
}
