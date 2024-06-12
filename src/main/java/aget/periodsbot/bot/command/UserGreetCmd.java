package aget.periodsbot.bot.command;

import aget.periodsbot.bot.convert.Convert;
import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.domain.usecase.FunctionUseCase;
import aget.periodsbot.dto.UserGreetRqDto;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

public class UserGreetCmd implements Cmd<Update, AbsSender> {
    private final FunctionUseCase<UserGreetRqDto, String> userGreet;
    private final Convert<Update, UserGreetRqDto> userGreetRqConvert;
    private final Convert<String, String> userGreetRsConvert;

    public UserGreetCmd(FunctionUseCase<UserGreetRqDto, String> userGreet,
                        Convert<Update, UserGreetRqDto> userGreetRqConvert,
                        Convert<String, String> userGreetRsConvert) {
        this.userGreet = userGreet;
        this.userGreetRqConvert = userGreetRqConvert;
        this.userGreetRsConvert = userGreetRsConvert;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        return Optional.of(new SendText(
            new SendMessage(
                update.getMessage().getFrom().getId().toString(),
                this.userGreetRsConvert.convert(
                    this.userGreet.handle(
                        this.userGreetRqConvert.convert(update)
                    )
                )
            )
        ));
    }
}
