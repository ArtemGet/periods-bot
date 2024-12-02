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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties for bot.
 *
 * @since 0.1.0
 */
public final class BotProps {
    /**
     * Profile.
     */
    private final String profile;

    public BotProps() {
        this(System.getenv("profile"));
    }

    public BotProps(final String profile) {
        this.profile = profile;
    }

    public String botName() {
        return this.property("bot.name");
    }

    public String botToken() {
        return this.property("bot.secret");
    }

    private String property(final String name) {
        try (
            InputStream inputStream =
                Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(this.profile)
        ) {
            final Properties properties = new Properties();
            properties.load(inputStream);
            return properties.getProperty(name);
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
