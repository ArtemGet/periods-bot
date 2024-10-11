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

public class FkPeriods implements Periods {

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
    public void add(LocalDate start) {
        this.periods.add(start);
    }

    @Override
    public List<Period> last(Integer amount) {
        List<Period> result = new ArrayList<>();
        if (!this.periods.isEmpty()) {
            List<LocalDate> per = this.periods
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
    public void remove(LocalDate start) {
        this.periods.removeIf(f -> f.isEqual(start));
    }

    @Override
    public boolean equals(Object object) {
        return this == object ||
            object instanceof FkPeriods
                && this.periods.equals(((FkPeriods) object).periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.periods);
    }
}
