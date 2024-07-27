package aget.periodsbot.bot.convert;

import java.time.LocalDate;

public class DateRsConvert implements Convert<LocalDate, String> {
    @Override
    public String convert(LocalDate source) {
        return String.format("Добавлено начало цикла: %s", source);
    }
}
