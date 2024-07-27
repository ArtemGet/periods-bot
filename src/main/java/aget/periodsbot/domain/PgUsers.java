package aget.periodsbot.domain;

import org.jdbi.v3.core.Handle;

import java.util.UUID;

public class PgUsers implements Users {
    private final Handle dataSource;
    private final PeriodsFactory periodsFactory;

    public PgUsers(Handle dataSource, PeriodsFactory periodsFactory) {
        this.dataSource = dataSource;
        this.periodsFactory = periodsFactory;
    }

    @Override
    public void add(Long userTelegramId, String name) {
        this.dataSource.useTransaction(
            handle ->
                handle.createUpdate("INSERT INTO public.users (id, t_id, name) VALUES (:id, :t_id, :name)")
                    .bind("id", UUID.randomUUID())
                    .bind("t_id", userTelegramId)
                    .bind("name", name)
                    .execute()
        );
    }

    @Override
    public User user(Long userTelegramId) {
        return this.dataSource.registerRowMapper(
            PgUser.class,
            (rs, ctx) ->
                new PgUser(
                    this.dataSource,
                    this.periodsFactory,
                    UUID.fromString(rs.getString("id"))
                )
        ).inTransaction(
            handle ->
                handle.select(
                    "SELECT id FROM public.users WHERE t_id = ?",
                    userTelegramId
                ).mapTo(PgUser.class)
        ).one();
    }
}
