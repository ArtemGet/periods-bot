package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.context.UsersContext;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

public class GreetCmd implements Cmd<Update, AbsSender> {
    private final UsersContext context;

    public GreetCmd(UsersContext context) {
        this.context = context;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update){
        this.context.consume(users ->
            users.add(
                update.getMessage().getFrom().getId(),
                update.getMessage().getFrom().getUserName()
            )
        );
        return Optional.of(new SendText(
            update,
            String.format("Приветствую, %s", update.getMessage().getFrom().getUserName())
        ));
    }
}
