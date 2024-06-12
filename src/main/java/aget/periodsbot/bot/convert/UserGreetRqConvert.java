package aget.periodsbot.bot.convert;

import aget.periodsbot.dto.UserGreetRqDto;
import aget.periodsbot.dto.UserTIdDto;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UserGreetRqConvert implements Convert<Update, UserGreetRqDto> {
    private final Convert<Update, UserTIdDto> userTIdConvert;

    public UserGreetRqConvert(Convert<Update, UserTIdDto> userTIdConvert) {
        this.userTIdConvert = userTIdConvert;
    }

    @Override
    public UserGreetRqDto convert(Update update) {
        return new UserGreetRqDto(
                this.userTIdConvert.convert(update),
                update.getMessage().getFrom().getUserName()
        );
    }
}
