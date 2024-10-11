package aget.periodsbot.domain;

import org.jdbi.v3.core.Jdbi;

import java.util.function.Consumer;
import java.util.function.Function;

public class PgTransaction implements Transaction<Users> {
    private final Jdbi db;
    private final PeriodsFactory periodsFactory;

    public PgTransaction(String url) {
        this(Jdbi.create(url));
    }

    public PgTransaction(Jdbi db) {
        this(db, new PgPeriodsFactory());
    }

    public PgTransaction(Jdbi db, PeriodsFactory periodsFactory) {
        this.db = db;
        this.periodsFactory = periodsFactory;
    }

    @Override
    public <R> R callback(Function<Users, R> fn) {
        return db.inTransaction(handle ->
            fn.apply(new PgUsers(handle, this.periodsFactory))
        );
    }

    @Override
    public void consume(Consumer<Users> fn) {
        db.useTransaction(handle ->
            fn.accept(new PgUsers(handle, this.periodsFactory))
        );
    }
}
