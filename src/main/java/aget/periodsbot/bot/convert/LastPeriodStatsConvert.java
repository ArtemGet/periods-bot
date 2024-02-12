package aget.periodsbot.bot.convert;

import aget.periodsbot.dto.LastPeriodStatsDto;

public class LastPeriodStatsConvert implements Convert<LastPeriodStatsDto, String> {
    @Override
    public String convert(LastPeriodStatsDto source) {
        return String.format(
                """
                        Начало цикла: %s. С начала цикла прошло %s дней.
                        До начала следующего цикла предположительно %s дней
                        """,
                source.cycleStart(),
                source.daysPassedFromCycleStart(),
                source.predictedDaysBeforeCycleEnd()
        );
    }
}
