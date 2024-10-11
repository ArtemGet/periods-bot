package aget.periodsbot.bot.command;

import aget.periodsbot.bot.FkUpdate;
import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.context.PgTransaction;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.jdbi.v3.testing.junit5.tc.JdbiTestcontainersExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Testcontainers
public class PeriodStatisticCmdTest {
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
    void execute_periodsArePresent_ShowPeriods(Jdbi jdbi) {
        Update update = new FkUpdate("test", 1L, "2").update();
        LocalDate last = LocalDate.of(2021, 12, 20);

        new PgTransaction(jdbi).consume(users -> {
            users.add(1L, "test");
            users.user(1L).periods().add(last);
            users.user(1L).periods().add(last.minusDays(5));
        });

        Assertions.assertEquals(
            new SendText(
                "20.12.2021 - " + DAYS.between(last, LocalDate.now()) +
                    "\n15.12.2021 - 5", update),
            new PeriodStatisticCmd(
                new PgTransaction(jdbi),
                Integer::parseInt,
                "dd.MM.yyyy"
            ).execute(update).orElseGet(() -> new SendText("no", update)));
    }

    @Test
    void execute_periodsNotPresent_ShowNothing(Jdbi jdbi) {
        Update update = new FkUpdate("test", 2L, "2").update();

        new PgTransaction(jdbi).consume(users -> users.add(2L, "test"));

        Assertions.assertEquals(
            new SendText("", update),
            new PeriodStatisticCmd(
                new PgTransaction(jdbi),
                "dd.MM.yyyy"
            ).execute(update).orElseGet(() -> new SendText("no", update)));
    }
}
