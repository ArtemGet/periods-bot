package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.Transaction;
import aget.periodsbot.domain.Periods;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PeriodStatisticCmd implements Cmd<Update, AbsSender> {

    private final Transaction<Users> transaction;
    private final Function<String, Integer> amountConvert;
    private final DateTimeFormatter formatter;


    public PeriodStatisticCmd(Transaction<Users> transaction,
                              String dateFormat) {
        this(transaction, 5, dateFormat);
    }

    public PeriodStatisticCmd(Transaction<Users> transaction,
                              Integer amount,
                              String dateFormat) {
        this(transaction, s -> amount, DateTimeFormatter.ofPattern(dateFormat));
    }

    public PeriodStatisticCmd(Transaction<Users> transaction,
                              Function<String, Integer> amountConvert,
                              String dateFormat) {
        this(transaction, amountConvert, DateTimeFormatter.ofPattern(dateFormat));
    }

    public PeriodStatisticCmd(Transaction<Users> transaction,
                              Function<String, Integer> amountConvert,
                              DateTimeFormatter formatter) {
        this.transaction = transaction;
        this.amountConvert = amountConvert;
        this.formatter = formatter;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        return Optional.of(
            new SendMsg(
                update,
                transaction.callback(
                    users ->
                        new Periods.SmartPeriods(users.user(update.getMessage().getFrom().getId()).periods())
                            .last(amountConvert.apply(update.getMessage().getText()))
                            .stream()
                            .map(p -> p.start().format(formatter) + " - " + p.days().toString())
                            .collect(Collectors.joining("\n"))
                )
            )
        );
    }
}
