package aget.periodsbot.domain;

import aget.periodsbot.repo.PgPeriods;
import aget.periodsbot.repo.PgPeriodsFactory;
import aget.periodsbot.repo.PgUsers;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
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

import java.util.UUID;

@Testcontainers
class PgUserTest {
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
    JdbiExtension extension = JdbiTestcontainersExtension.instance(dbContainer);

    @Test
    void name_shouldReturnDefault_whenUserIsNotPresent(Jdbi jdbi) {
        Assertions.assertEquals(
                "пользователь",
                jdbi.inTransaction(
                        handle -> new PgUser(
                                handle,
                                new PgPeriodsFactory(),
                                UUID.randomUUID()
                        ).name()
                )
        );
    }

    @Test
    void name_shouldReturn_whenUserIsPresent(Jdbi jdbi) {
        User test = jdbi.inTransaction(
                handle -> new PgUsers(
                        handle,
                        new PgPeriodsFactory()
                ).add(1L, "test")
        );

        Assertions.assertEquals(
                "test",
                jdbi.inTransaction(
                        handle -> new PgUser(
                                handle,
                                new PgPeriodsFactory(),
                                test.id()
                        ).name()
                )
        );
    }

    @Test
    void name_shouldReturnDefault_whenUserIsPresentAndNameIsNotPresent(Jdbi jdbi) {
        jdbi.inTransaction(
                handle -> new PgUsers(
                        handle,
                        new PgPeriodsFactory()
                ).add(2L, null)
        );

        Assertions.assertEquals(
                "пользователь",
                jdbi.inTransaction(
                        handle -> new PgUser(
                                handle,
                                new PgPeriodsFactory(),
                                UUID.randomUUID()
                        ).name()
                )
        );
    }

    @Test
    void periods(Handle handle) {
        User user =
                new PgUsers(
                        handle,
                        new PgPeriodsFactory()
                ).add(3L, "test");

        Assertions.assertEquals(
                new PgPeriods(handle, user.id()),
                user.periods()
        );
    }
}
