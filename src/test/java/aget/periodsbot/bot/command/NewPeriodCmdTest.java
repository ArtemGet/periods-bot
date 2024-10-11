package aget.periodsbot.bot.command;

import aget.periodsbot.bot.FkUpdate;
import aget.periodsbot.bot.convert.StringToDateConvert;
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

@Testcontainers
public class NewPeriodCmdTest {
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
    void execute_PeriodIsNotPresent_addsNew(Jdbi jdbi) {
        Update update = new FkUpdate("test", 1L, "20-12-2021").update();

        new PgTransaction(jdbi).consume(users -> users.add(1L, "test"));

        Assertions.assertEquals(
            new SendText("Есть, мэм!", update),
            new NewPeriodCmd(
                new PgTransaction(jdbi),
                new StringToDateConvert("dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}")
            ).execute(update).orElseGet(() -> new SendText("no", update)));
    }

    @Test
    void execute_PeriodIsPresent_addsNew(Jdbi jdbi) {
        Update update = new FkUpdate("test", 2L, "20-12-2021").update();

        new PgTransaction(jdbi).consume(users -> users.add(2L, "test"));
        NewPeriodCmd cmd = new NewPeriodCmd(
            new PgTransaction(jdbi),
            new StringToDateConvert("dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}")
        );
        cmd.execute(update);

        Assertions.assertEquals(
            new SendText("Есть, мэм!", update),
            cmd.execute(update).orElseGet(() -> new SendText("no", update)));
    }
}
