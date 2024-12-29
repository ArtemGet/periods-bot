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

package aget.periodsbot.bot.convert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts string to {@link LocalDate}.
 *
 * @since 0.1.0
 */
public final class StringToDateConvert implements Function<String, LocalDate> {
    /**
     * Date format.
     */
    private final DateTimeFormatter format;

    /**
     * Pattern.
     */
    private final Pattern pattern;

    public StringToDateConvert(final String format, final String pattern) {
        this(format, Pattern.compile(pattern));
    }

    public StringToDateConvert(final String format, final Pattern pattern) {
        this(DateTimeFormatter.ofPattern(format), pattern);
    }

    public StringToDateConvert(final DateTimeFormatter format, final String pattern) {
        this(format, Pattern.compile(pattern));
    }

    public StringToDateConvert(final DateTimeFormatter format, final Pattern pattern) {
        this.format = format;
        this.pattern = pattern;
    }

    @Override
    public LocalDate apply(final String input) {
        final Matcher matcher = this.pattern.matcher(input);
        matcher.find();
        return LocalDate.parse(matcher.group(), this.format);
    }
}
