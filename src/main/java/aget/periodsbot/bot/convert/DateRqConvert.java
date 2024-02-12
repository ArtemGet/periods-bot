package aget.periodsbot.bot.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateRqConvert implements Convert<String, Date> {
    private final String prefixTrim;

    public DateRqConvert(String prefixTrim) {
        this.prefixTrim = prefixTrim;
    }

    public DateRqConvert() {
        this.prefixTrim = "";
    }

    @Override
    public Date convert(String source) {
        try {
            return new SimpleDateFormat("MM-dd-yyyy").parse(
                    source.replace(this.prefixTrim, "")
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
