package aget.periodsbot.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FkPeriods implements Periods {

    private final List<LocalDate> periods;

    public FkPeriods() {
        this(Collections.emptyList());
    }

    public FkPeriods(final LocalDate... periods) {
        this(Arrays.asList(periods));
    }

    public FkPeriods(final List<LocalDate> periods) {
        this.periods = periods;
    }

    @Override
    public void add(LocalDate start) {
        periods.add(start);
    }

    @Override
    public List<Period> last(Integer amount) {
        List<LocalDate> per = periods.stream()
            .sorted()
            .skip(Math.max(periods.size() - amount, 0)).toList();

        List<Period> result = new ArrayList<>();

        for (int i = 0; i < per.size(); i++) {
            if (i + 1 == per.size()) {
                result.add(new EaPeriod(per.get(i)));
            } else {
                result.add(new EaPeriod(per.get(i), per.get(i + 1)));
            }
        }
        return result;
    }

    @Override
    public void remove(LocalDate start) {
        periods.removeIf(f -> f.isEqual(start));
    }
}
