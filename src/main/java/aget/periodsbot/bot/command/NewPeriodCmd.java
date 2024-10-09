package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.context.UsersContext;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public class NewPeriodCmd implements Cmd<Update, AbsSender> {
    private final UsersContext context;
    private final Function<String, LocalDate> convert;

    public NewPeriodCmd(UsersContext context, Function<String, LocalDate> convert) {
        this.context = context;
        this.convert = convert;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update){
        context.consume(users ->
            users.user(update.getMessage().getFrom().getId())
                .periods()
                .add(convert.apply(
                    update.getMessage().getText()
                ))
        );
        return Optional.of(new SendText("Есть, мэм!", update));
    }
}
