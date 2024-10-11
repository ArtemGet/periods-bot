package aget.periodsbot.domain;

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
                MountableFile.forClasspathResource("db/init.sql"),
                "/docker-entrypoint-initdb.d/"
            );

    @RegisterExtension
    static JdbiExtension extension = JdbiTestcontainersExtension.instance(dbContainer);

    @Test
    void name_userIsNotPresent_returnsDefault(Jdbi jdbi) {
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
    void name_userIsPresent_returnsName(Jdbi jdbi) {
        Transaction<Users> transaction = new PgTransaction(jdbi);

        transaction.consume(users -> users.add(1L, "test"));

        Assertions.assertEquals(
            "test",
            transaction.callback(users -> users.user(1L).name())
        );
    }

    @Test
    void name_userIsPresentAndNameIsNotPresent_returnsDefault(Jdbi jdbi) {
        Transaction<Users> transaction = new PgTransaction(jdbi);
        transaction.consume(users -> users.add(2L, null));

        Assertions.assertEquals(
            "пользователь",
            transaction.callback(users -> users.user(2L).name())
        );
    }
}
