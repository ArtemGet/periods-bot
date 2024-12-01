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

package aget.periodsbot.domain.fake;

import aget.periodsbot.domain.EaPeriod;
import aget.periodsbot.domain.Period;
import aget.periodsbot.domain.Periods;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class FkPeriods implements Periods {

    private final List<LocalDate> periods;

    public FkPeriods() {
        this(new ArrayList<>());
    }

    public FkPeriods(final LocalDate... periods) {
        this(Arrays.asList(periods));
    }

    public FkPeriods(final List<LocalDate> periods) {
        this.periods = periods;
    }

    @Override
    public void add(final LocalDate start) {
        this.periods.add(start);
    }

    @Override
    public List<Period> last(final Integer amount) {
        final List<Period> result = new ArrayList<>();
        if (!this.periods.isEmpty()) {
            final List<LocalDate> per = this.periods
                .stream()
                .sorted(Comparator.reverseOrder())
                .limit(amount).toList();

            result.add(new EaPeriod(per.get(0)));

            for (int i = 1; i < per.size(); i++) {
                result.add(new EaPeriod(per.get(i), per.get(i - 1)));
            }
        }
        return result;
    }

    @Override
    public void remove(final LocalDate start) {
        this.periods.removeIf(f -> f.isEqual(start));
    }

    @Override
    public boolean equals(final Object object) {
        return this == object ||
            object instanceof FkPeriods
                && this.periods.equals(((FkPeriods) object).periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.periods);
    }
}
