package aget.periodsbot.bot.convert;

import aget.periodsbot.dto.PeriodAddDto;
import aget.periodsbot.dto.UserTIdDto;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Date;

public class PeriodAddConvert implements Convert<Message, PeriodAddDto> {
    private final Convert<String, Date> dateConvert;

    public PeriodAddConvert(Convert<String, Date> dateConvert) {
        this.dateConvert = dateConvert;
    }

    @Override
    public PeriodAddDto convert(Message source) {
        return new PeriodAddDto(
                new UserTIdDto(source.getFrom().getId()),
                this.dateConvert.convert(source.getText())
        );
    }
}
