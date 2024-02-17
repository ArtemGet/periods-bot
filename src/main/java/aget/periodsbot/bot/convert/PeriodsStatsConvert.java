package aget.periodsbot.bot.convert;

import aget.periodsbot.dto.LastPeriodStatsDto;
import aget.periodsbot.dto.PeriodsStatsDto;

import java.util.Optional;
import java.util.stream.Collectors;

public class PeriodsStatsConvert implements Convert<Optional<PeriodsStatsDto>, String> {
    private final Convert<LastPeriodStatsDto, String> lastPeriodStatsConvert;

    public PeriodsStatsConvert(Convert<LastPeriodStatsDto, String> lastPeriodStatsConvert) {
        this.lastPeriodStatsConvert = lastPeriodStatsConvert;
    }

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
                                .concat(this.lastPeriodStatsConvert.convert(src.lastPeriod()))
                                .concat("\n")
                                .concat(
                                        String.format(
                                                "Средняя продолжительность цикла: %s",
                                                src.avgPeriodLength()
                                        )
                                )
                ).orElse(
                        """
                                В вашем дневнике нет записей.
                                Добавить цикл можно воспользовавшись командой:
                                добавить цикл MM/dd/yyyy
                                """
                );
    }
}
