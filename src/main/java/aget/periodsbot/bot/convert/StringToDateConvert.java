package aget.periodsbot.bot.convert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringToDateConvert implements Function<String, LocalDate> {
    private final DateTimeFormatter format;
    private final Pattern pattern;

    public StringToDateConvert(String format, String pattern) {
        this(format, Pattern.compile(pattern));
    }

    public StringToDateConvert(String format, Pattern pattern) {
        this(DateTimeFormatter.ofPattern(format), pattern);
    }

    public StringToDateConvert(DateTimeFormatter format, String pattern) {
        this(format, Pattern.compile(pattern));
    }

    public StringToDateConvert(DateTimeFormatter format, Pattern pattern) {
        this.format = format;
        this.pattern = pattern;
    }

    @Override
    public LocalDate apply(String s) {
        Matcher matcher = pattern.matcher(s);
        matcher.find();
        return LocalDate.parse(matcher.group(), format);
    }
}
