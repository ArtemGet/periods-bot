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
