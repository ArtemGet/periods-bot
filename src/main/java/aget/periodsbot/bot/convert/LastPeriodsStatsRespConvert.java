package aget.periodsbot.bot.convert;

import aget.periodsbot.dto.PeriodsStatsDto;

import java.util.Optional;
import java.util.stream.Collectors;

public class LastPeriodsStatsRespConvert implements Convert<Optional<PeriodsStatsDto>, String> {
    @Override
    public String convert(Optional<PeriodsStatsDto> source) {
        return source
                .map(src ->
                        src.periodsStats().stream()
                                .map(periodStats ->
                                        String.format(
                                                "Начало цикла: %s, конец цикла: %s",
                                                periodStats.cycleStart(),
                                                periodStats.cycleEnd()
                                        )
                                )
                                .collect(Collectors.joining("\n"))
                                .concat(
                                        String.format(
                                                        """
                                                        Начало цикла: %s. С начала цикла прошло %s дней.
                                                        До начала следующего цикла предположительно %s дней.
                                                        """,
                                                src.lastPeriod().cycleStart(),
                                                src.lastPeriod().daysPassedFromCycleStart(),
                                                src.lastPeriod().predictedDaysBeforeCycleEnd()
                                        )
                                )
                                .concat("\n")
                                .concat(
                                        String.format(
                                                "Средняя продолжительность цикла: %s",
                                                src.avgPeriodLength()
                                        )
                                )
                )
                .orElse(
                        """
                                В вашем дневнике нет записей.
                                Добавить цикл можно воспользовавшись командой:
                                добавить цикл MM/dd/yyyy
                                """
                );
    }
}
