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
import java.util.List;

public interface Periods {
    void add(LocalDate start);

    List<Period> last(Integer amount);

    void remove(LocalDate start);

    class SmartPeriods implements Periods {
        private final Periods periods;

        public SmartPeriods(Periods periods) {
            this.periods = periods;
        }

        @Override
        public void add(LocalDate start) {
            periods.add(start);
        }

        @Override
        public List<Period> last(Integer amount) {
            return periods.last(amount);
        }

        @Override
        public void remove(LocalDate start) {
            periods.remove(start);
        }

        public Period current() {
            return periods.last(1).get(0);
        }

        public Integer avgLength(Integer count) {
            return Double
                .valueOf(
                    periods.last(count + 1)
                        .stream()
                        .skip(1)
                        .mapToLong(Period::days)
                        .average()
                        .orElse(Double.NaN)
                ).intValue();
        }
    }
}
