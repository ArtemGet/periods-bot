package aget.periodsbot.bot.convert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateRqConvert implements Convert<String, LocalDate> {
    private final String prefixTrim;

    public DateRqConvert(String prefixTrim) {
        this.prefixTrim = prefixTrim;
    }

    public DateRqConvert() {
        this.prefixTrim = "";
    }

    @Override
    public LocalDate convert(String source) {
        try {
            return LocalDate.parse(source, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }
}
