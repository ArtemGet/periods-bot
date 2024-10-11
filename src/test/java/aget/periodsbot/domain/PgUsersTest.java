package aget.periodsbot.domain;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.jdbi.v3.testing.junit5.tc.JdbiTestcontainersExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
class PgUsersTest {
    @Container
    public static final JdbcDatabaseContainer<?> dbContainer =
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withReuse(false)
            .withDatabaseName("periods_bot")
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("db/init.sql"),
                "/docker-entrypoint-initdb.d/"
            );

    @RegisterExtension
    static JdbiExtension extension = JdbiTestcontainersExtension.instance(dbContainer);

    @Test
    void add_userIsNotPresent_addsNew(Jdbi jdbi) {
        Assertions.assertDoesNotThrow(
            () -> jdbi.useTransaction(
                handle ->
                    new PgUsers(
                        handle,
                        new PgPeriodsFactory()
                    ).add(1L, "test")
            )
        );
    }

    @Test
    void add_userIsPresent_throws(Jdbi jdbi) {
        Assertions.assertDoesNotThrow(
            () -> jdbi.useTransaction(
                handle ->
                    new PgUsers(
                        handle,
                        new PgPeriodsFactory()
                    ).add(2L, "test")
            )
        );

        Assertions.assertThrows(
            UnableToExecuteStatementException.class,
            () -> jdbi.useTransaction(
                handle ->
                    new PgUsers(
                        handle,
                        new PgPeriodsFactory()
                    ).add(2L, "test")
            )
        );
    }

    @Test
    void user_userIsPresent_returnsUser(Jdbi jdbi) {
        Transaction<Users> transaction = new PgTransaction(jdbi);
        transaction.consume(users -> users.add(3L, "test"));

        Assertions.assertDoesNotThrow(
            () -> transaction.callback(users -> users.user(3L))
        );
    }

    @Test
    void user_userIsNotPresent_throws(Jdbi jdbi) {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> new PgTransaction(jdbi)
                .callback(users -> users.user(4L))
        );
    }
}
