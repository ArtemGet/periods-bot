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

@Testcontainers
public class GreetCmdTest {
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
    void execute_userIsNotPresent_greetsUser(Jdbi jdbi) {
        Update update = new FkUpdate("test", 1L, "text").update();
        Assertions.assertEquals(
            new SendText("Приветствую, test!", update),
            new GreetCmd(
                new PgTransaction(jdbi)
            ).execute(update).orElseGet(() -> new SendText("no", update)));
    }

    @Test
    void execute_userIsPresent_greetsUserAgain(Jdbi jdbi) {
        Update update = new FkUpdate("test", 1L, "text").update();
        GreetCmd cmd = new GreetCmd(
            new PgTransaction(jdbi)
        );

        cmd.execute(update);

        Assertions.assertEquals(
            new SendText("Приветствую вновь, test!", update),
            cmd.execute(update).orElseGet(() -> new SendText("no", update)));
    }
}
