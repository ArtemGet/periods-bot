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

/**
 * Eager user's period.
 *
 * @since 0.1.0
 */
public final class EaPeriod implements Period {
    /**
     * Period start.
     */
    private final LocalDate strt;

    /**
     * Period end.
     */
    private final LocalDate end;

    public EaPeriod(final LocalDate start) {
        this(start, LocalDate.now());
    }

    public EaPeriod(final LocalDate start, final Period next) {
        this(start, next.start());
    }

    public EaPeriod(final LocalDate start, final LocalDate end) {
        this.strt = start;
        this.end = end;
    }

    @Override
    public LocalDate start() {
        return this.strt;
    }

    @Override
    public Integer days() {
        return Long
            .valueOf(Math.abs(ChronoUnit.DAYS.between(this.strt, this.end)))
            .intValue() + 1;
    }
}
