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

package aget.periodsbot.config;

import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * Properties for Liquibase.
 *
 * @since 0.1.0
 */
public final class LiquibaseConf {
    /**
     * Liquibase changelog file.
     */
    private final String changelog;

    public LiquibaseConf(final String changelog) {
        this.changelog = changelog;
    }

    public void migrate(final String url) {
        try {
            new Liquibase(
                this.changelog,
                new ClassLoaderResourceAccessor(),
                DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(
                        new JdbcConnection(DriverManager.getConnection(url))
                    )
            ).update(new Contexts(), new LabelExpression());
        } catch (final SQLException | LiquibaseException exception) {
            throw new IllegalArgumentException(
                String.format(
                    "Can't migrate database with url %s and changelog file %s",
                    url,
                    this.changelog
                ),
                exception
            );
        }
    }
}
