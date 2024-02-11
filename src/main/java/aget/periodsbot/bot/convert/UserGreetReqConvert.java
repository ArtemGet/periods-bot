package aget.periodsbot.bot.convert;

import aget.periodsbot.dto.UserGreetRqDto;
import aget.periodsbot.dto.UserTIdDto;
import org.telegram.telegrambots.meta.api.objects.Message;

public class UserGreetReqConvert implements Convert<Message, UserGreetRqDto> {
    @Override
    public UserGreetRqDto convert(Message message) {
        return new UserGreetRqDto(
                new UserTIdDto(message.getFrom().getId()),
                message.getFrom().getUserName());
    }
}
