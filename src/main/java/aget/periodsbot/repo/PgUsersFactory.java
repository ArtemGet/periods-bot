package aget.periodsbot.repo;

import org.jdbi.v3.core.Handle;

public class PgUsersFactory implements UsersFactory {
    private final PeriodsFactory periodsFactory;

    public PgUsersFactory(PeriodsFactory periodsFactory) {
        this.periodsFactory = periodsFactory;
    }

    public PgUsersFactory() {
        this.periodsFactory = new EaPeriodsFactory();
    }

    @Override
    public Users provide(Handle ds) {
        return new PgUsers(ds, this.periodsFactory);
    }
}
