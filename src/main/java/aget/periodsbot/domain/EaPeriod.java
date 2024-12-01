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
import java.time.temporal.ChronoUnit;

public class EaPeriod implements Period {
    private final LocalDate start;
    private final LocalDate end;

    public EaPeriod(LocalDate start) {
        this(start, LocalDate.now());
    }

    public EaPeriod(LocalDate start, Period next) {
        this(start, next.start());
    }

    public EaPeriod(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public LocalDate start() {
        return start;
    }

    @Override
    public Integer days() {
        return Long
            .valueOf(Math.abs(ChronoUnit.DAYS.between(start,end)))
            .intValue() + 1;
    }
}
