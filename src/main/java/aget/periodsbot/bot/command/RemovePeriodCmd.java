package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.context.Transaction;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public class RemovePeriodCmd implements Cmd<Update, AbsSender> {
    private final Transaction<Users> transaction;
    private final Function<String, LocalDate> convert;

    public RemovePeriodCmd(Transaction<Users> transaction, Function<String, LocalDate> convert) {
        this.transaction = transaction;
        this.convert = convert;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        transaction.consume(users ->
            users.user(update.getMessage().getFrom().getId())
                .periods()
                .remove(convert.apply(
                    update.getMessage().getText()
                ))
        );
        return Optional.of(new SendText("Есть, мэм!", update));
    }
}
