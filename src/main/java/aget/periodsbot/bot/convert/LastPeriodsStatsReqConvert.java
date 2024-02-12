package aget.periodsbot.bot.convert;

import aget.periodsbot.dto.UserTIdDto;
import org.telegram.telegrambots.meta.api.objects.Message;

public class LastPeriodsStatsReqConvert implements Convert<Message, UserTIdDto> {
    @Override
    public UserTIdDto convert(Message source) {
        return new UserTIdDto(source.getFrom().getId());
    }
}
