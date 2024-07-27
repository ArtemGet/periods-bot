package aget.periodsbot.convert;

import aget.periodsbot.bot.convert.DateConvert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class DateConvertTest {
    @Test
    public void convert_stringWithDate_returnsDate() {
        Assertions.assertEquals(
            LocalDate.of(2020, 6, 3),
            new DateConvert("dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}")
                .apply("Тестовая строка 03-06-2020")
        );
    }

    @Test
    public void convert_stringWithoutDate_throws() {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> new DateConvert("dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}")
                .apply("Тестовая строка 03-06-200")
        );
    }
}
