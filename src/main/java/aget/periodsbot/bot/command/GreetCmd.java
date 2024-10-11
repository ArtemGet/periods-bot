package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.context.Transaction;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
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
        Optional<Send<AbsSender>> send;
        try {
            transaction.consume(users ->
                users.add(
                    update.getMessage().getFrom().getId(),
                    update.getMessage().getFrom().getUserName()
                )
            );
            send = Optional.of(new SendText(
                String.format("Приветствую, %s!", update.getMessage().getFrom().getUserName()),
                update
            ));
        } catch (UnableToExecuteStatementException e) {
            send = Optional.of(new SendText(
                String.format("Приветствую вновь, %s!", update.getMessage().getFrom().getUserName()),
                update
            ));
        }
        return send;
    }
}
