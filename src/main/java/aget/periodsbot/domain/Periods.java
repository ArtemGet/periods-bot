package aget.periodsbot.domain;

import java.util.Date;
import java.util.List;

public interface Periods {
    void add(Date start);

    List<Period> last(Integer amount);

    void remove(Date start);

    class SmartPeriods implements Periods {
        private final Periods periods;

        public SmartPeriods(Periods periods) {
            this.periods = periods;
        }

        @Override
        public void add(Date start) {
            periods.add(start);
        }

        @Override
        public List<Period> last(Integer amount) {
            return periods.last(amount);
        }

        @Override
        public void remove(Date start) {
            periods.remove(start);
        }

        public Period current() {
            return periods.last(1).get(0);
        }

        public Integer avgLength(Integer count) {
            return Double
                .valueOf(
                    periods.last(count)
                        .stream()
                        .mapToLong(Period::days)
                        .average()
                        .orElse(Double.NaN)
                ).intValue();
        }
    }
}
