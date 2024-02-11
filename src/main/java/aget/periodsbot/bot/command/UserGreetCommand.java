package aget.periodsbot.bot.command;

import aget.periodsbot.bot.convert.Convert;
import aget.periodsbot.bot.send.Send;
import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.domain.usecase.UserGreet;
import aget.periodsbot.dto.UserGreetRqDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class UserGreetCommand extends TextCommand {
    private final UserGreet userGreet;
    private final Convert<Message, UserGreetRqDto> userGreetReqConvert;
    private final Convert<String, String> userGreetRespConvert;

    public UserGreetCommand(String trigger,
                            UserGreet userGreet,
                            Convert<Message, UserGreetRqDto> userGreetReqConvert,
                            Convert<String, String> userGreetRespConvert) {
        super(trigger);
        this.userGreet = userGreet;
        this.userGreetReqConvert = userGreetReqConvert;
        this.userGreetRespConvert = userGreetRespConvert;
    }

    @Override
    public Send execute(Message message) {
        return new SendText(
                new SendMessage(
                        message.getFrom().getId().toString(),
                        this.userGreetRespConvert.convert(
                                this.userGreet.handle(
                                        this.userGreetReqConvert.convert(message)))));
    }
}
