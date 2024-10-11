package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.Transaction;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

public class GreetCmd implements Cmd<Update, AbsSender> {
    private final Transaction<Users> transaction;

    public GreetCmd(Transaction<Users> transaction) {
        this.transaction = transaction;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        transaction.consume(users ->
            users.add(
                update.getMessage().getFrom().getId(),
                update.getMessage().getFrom().getUserName()
            )
        );
        return Optional.of(new SendMsg(
            update,
            String.format("Приветствую, %s!", update.getMessage().getFrom().getUserName())
        ));
    }
}
