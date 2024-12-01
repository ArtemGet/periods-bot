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

package aget.periodsbot.convert;

import aget.periodsbot.bot.convert.StringToDateConvert;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class StringToDateConvertTest {
    @Test
    void shouldProvideDate() {
        Assertions.assertEquals(
            LocalDate.of(2020, 6, 3),
            new StringToDateConvert("dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}")
                .apply("Тестовая строка 03-06-2020")
        );
    }

    @Test
    void throwsWhenWrongFormatProvided() {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> new StringToDateConvert("dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}")
                .apply("Тестовая строка 03-06-200")
        );
    }
}
