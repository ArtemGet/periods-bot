package aget.periodsbot.bot.convert;

import aget.periodsbot.dto.UserTIdDto;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UserTIdConvert implements Convert<Update, UserTIdDto> {
    @Override
    public UserTIdDto convert(Update source) {
        return new UserTIdDto(source.getMessage().getFrom().getId());
    }
}
