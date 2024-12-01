/*
 * MIT License
 *
 * Copyright (c) 2024 Artem Getmanskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package aget.periodsbot.domain;

import java.time.LocalDate;
import java.util.UUID;
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
final class PgPeriodsTest {
    @Container
    private static final JdbcDatabaseContainer<?> DB_CONTAINER =
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withReuse(false)
            .withDatabaseName("periods_bot")
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("db/init.sql"),
                "/docker-entrypoint-initdb.d/"
            );

    @RegisterExtension
    private final static JdbiExtension EXTENSION = JdbiTestcontainersExtension
        .instance(PgPeriodsTest.DB_CONTAINER);

    @Test
    void shouldAddPeriodWhenUserIsPresent(final Jdbi jdbi) {
        final Transaction<Users> transaction = new PgTransaction(jdbi);
        transaction.consume(users -> users.add(1L, "test"));

        Assertions.assertDoesNotThrow(() ->
            transaction.callback(users -> users.user(1L))
        );
    }

    @Test
    void throwErrorWhenUserIsNotPresent(final Jdbi jdbi) {
        Assertions.assertThrows(
            UnableToExecuteStatementException.class,
            () -> jdbi.useTransaction(
                handle -> new PgPeriods(
                    handle,
                    UUID.randomUUID()
                ).add(LocalDate.now())
            )
        );
    }

    @Test
    void shouldReturnLastPeriods(final Jdbi jdbi) {
        final Transaction<Users> transaction = new PgTransaction(jdbi);
        final LocalDate current = LocalDate.now();

        transaction.consume(users -> {
            users.add(2L, "test");
            users.user(2L).periods().add(current);
        });

        Assertions.assertEquals(
            current,
            transaction.callback(users ->
                users.user(2L)
                    .periods()
                    .last(10)
            ).get(0).start()
        );
    }

    @Test
    void shouldRemovePeriod(final Jdbi jdbi) {
        final Transaction<Users> transaction = new PgTransaction(jdbi);
        final LocalDate current = LocalDate.now();

        transaction.consume(users -> {
            users.add(3L, "test");
            users.user(3L).periods().add(current);
        });

        Assertions.assertDoesNotThrow(
            () -> transaction.consume(
                users -> users.user(3L).periods().remove(current)
            )
        );
    }
}
