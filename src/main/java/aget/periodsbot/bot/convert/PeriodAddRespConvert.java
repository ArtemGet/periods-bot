package aget.periodsbot.bot.convert;

import java.util.Date;

public class PeriodAddRespConvert implements Convert<Date, String> {
    @Override
    public String convert(Date source) {
        return String.format("Добавлено начало цикла: %s", source);
    }
}
