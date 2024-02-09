package aget.periodsbot.repo;

import aget.periodsbot.domain.TUser;
import aget.periodsbot.domain.User;
import org.jdbi.v3.core.Handle;

import java.util.UUID;

public class TUsers implements Users {
    private final Handle dataSource;
    private final PeriodsFactory periodsFactory;

    public TUsers(Handle dataSource, PeriodsFactory periodsFactory) {
        this.dataSource = dataSource;
        this.periodsFactory = periodsFactory;
    }

    @Override
    public User add(Long userTelegramId, String name) {
        return this.dataSource.registerRowMapper(TUser.class,
                        (rs, ctx) -> new TUser(this.dataSource,
                                this.periodsFactory,
                                UUID.fromString(rs.getString("id"))))
                .inTransaction(handle ->
                        handle.createUpdate("INSERT INTO users (t_id, name) VALUES (?, ?)")
                                .bind("t_id", userTelegramId)
                                .bind("name", name)
                                .executeAndReturnGeneratedKeys("id")
                                .mapTo(TUser.class))
                .first();
    }

    @Override
    public User user(Long userTelegramId) {
        return this.dataSource.registerRowMapper(TUser.class, (rs, ctx) -> new TUser(this.dataSource,
                        this.periodsFactory,
                        UUID.fromString(rs.getString("id"))))
                .inTransaction(handle ->
                        handle.select("SELECT id,name FROM users WHERE t-id = ?", userTelegramId)
                                .mapTo(TUser.class))
                .first();
    }
}
