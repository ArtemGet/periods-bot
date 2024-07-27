package aget.periodsbot.bot.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateConvert implements Function<String, LocalDate> {
    private final SimpleDateFormat format;
    private final Pattern pattern;

    public DateConvert(String format, String pattern) {
        this(format, Pattern.compile(pattern));
    }

    public DateConvert(String format, Pattern pattern) {
        this(new SimpleDateFormat(format), pattern);
    }

    public DateConvert(SimpleDateFormat format, Pattern pattern) {
        this.format = format;
        this.pattern = pattern;
    }

    @Override
    public LocalDate apply(String s) {
        try {
            Matcher matcher = pattern.matcher(s);
            matcher.find();
            return format.parse(matcher.group())
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
