package aget.periodsbot.bot.convert;

import aget.periodsbot.dto.UserGreetRqDto;
import aget.periodsbot.dto.UserTIdDto;
import org.telegram.telegrambots.meta.api.objects.Message;

public class UserGreetRqConvert implements Convert<Message, UserGreetRqDto> {
    private final Convert<Message, UserTIdDto> userTIdConvert;

    public UserGreetRqConvert(Convert<Message, UserTIdDto> userTIdConvert) {
        this.userTIdConvert = userTIdConvert;
    }

    @Override
    public UserGreetRqDto convert(Message message) {
        return new UserGreetRqDto(
                this.userTIdConvert.convert(message),
                message.getFrom().getUserName()
        );
    }
}
