package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.SendMsg;
import aget.periodsbot.domain.Transaction;
import aget.periodsbot.domain.Period;
import aget.periodsbot.domain.Periods;
import aget.periodsbot.domain.Users;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CurrentPeriodCmd implements Cmd<Update, AbsSender> {
    private final Transaction<Users> transaction;
    private final DateTimeFormatter formatter;

    public CurrentPeriodCmd(Transaction<Users> transaction, String format) {
        this(transaction, DateTimeFormatter.ofPattern(format));
    }

    public CurrentPeriodCmd(Transaction<Users> transaction, DateTimeFormatter formatter) {
        this.transaction = transaction;
        this.formatter = formatter;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        return Optional.of(
            new SendMsg(
                update,
                transaction.callback(
                    users -> {
                        Periods.SmartPeriods smartPeriods = new Periods.SmartPeriods(
                            users.user(update.getMessage().getFrom().getId()).periods()
                        );
                        Period current = smartPeriods.current();
                        return String.format("Началось: %s\nДень: %s\nОсталось: %s",
                            current.start().format(formatter),
                            current.days().toString(),
                            smartPeriods.avgLength(10) - current.days());
                    }
                )
            )
        );
    }
}
