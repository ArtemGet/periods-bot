package aget.periodsbot.repo;

import aget.periodsbot.domain.PgPeriodsFactory;
import aget.periodsbot.domain.PgUsers;
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
                            MountableFile.forClasspathResource("db/init-users.sql"),
                            "/docker-entrypoint-initdb.d/"
                    );

    @RegisterExtension
    static JdbiExtension extension = JdbiTestcontainersExtension.instance(dbContainer);

    @Test
    void add_shouldAdd_whenUserIsNotPresent(Jdbi jdbi) {
        Assertions.assertDoesNotThrow(
                () -> jdbi.inTransaction(
                        handle ->
                                new PgUsers(
                                        handle,
                                        new PgPeriodsFactory()
                                ).add(1L, "test")
                )
        );
    }

    @Test
    void add_shouldThrow_whenUserIsPresent(Jdbi jdbi) {
        Assertions.assertDoesNotThrow(
                () -> jdbi.inTransaction(
                        handle ->
                                new PgUsers(
                                        handle,
                                        new PgPeriodsFactory()
                                ).add(2L, "test")
                )
        );

        Assertions.assertThrows(
                UnableToExecuteStatementException.class,
                () -> jdbi.inTransaction(
                        handle ->
                                new PgUsers(
                                        handle,
                                        new PgPeriodsFactory()
                                ).add(2L, "test")
                )
        );
    }

    @Test
    void user_shouldNotThrow_whenUserIsPresent(Jdbi jdbi) {
        jdbi.inTransaction(
                handle ->
                        new PgUsers(
                                handle,
                                new PgPeriodsFactory()
                        ).add(3L, "test")
        );

        Assertions.assertDoesNotThrow(
                () -> jdbi.inTransaction(
                        handle ->
                                new PgUsers(
                                        handle,
                                        new PgPeriodsFactory()
                                ).user(3L)
                )
        );
    }

    @Test
    void user_shouldThrow_whenUserIsNotPresent(Jdbi jdbi) {
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> jdbi.inTransaction(
                        handle ->
                                new PgUsers(
                                        handle,
                                        new PgPeriodsFactory()
                                ).user(4L)
                )
        );
    }
}
