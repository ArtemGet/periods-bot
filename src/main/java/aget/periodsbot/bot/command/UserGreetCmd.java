package aget.periodsbot.bot.command;

import aget.periodsbot.bot.convert.Convert;
import aget.periodsbot.bot.send.Send;
import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.domain.usecase.FunctionUseCase;
import aget.periodsbot.dto.UserGreetRqDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class UserGreetCmd implements Cmd<Message, Send> {
    private final FunctionUseCase<UserGreetRqDto, String> userGreet;
    private final Convert<Message, UserGreetRqDto> userGreetRqConvert;
    private final Convert<String, String> userGreetRsConvert;

    public UserGreetCmd(FunctionUseCase<UserGreetRqDto, String> userGreet,
                        Convert<Message, UserGreetRqDto> userGreetRqConvert,
                        Convert<String, String> userGreetRsConvert) {
        this.userGreet = userGreet;
        this.userGreetRqConvert = userGreetRqConvert;
        this.userGreetRsConvert = userGreetRsConvert;
    }

    @Override
    public Send execute(Message message) {
        return new SendText(
                new SendMessage(
                        message.getFrom().getId().toString(),
                        this.userGreetRsConvert.convert(
                                this.userGreet.handle(
                                        this.userGreetRqConvert.convert(message)
                                )
                        )
                )
        );
    }
}
