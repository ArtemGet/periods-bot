package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.context.UsersContext;
import aget.periodsbot.domain.Periods;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PeriodStatisticCmd implements Cmd<Update, AbsSender> {

    private final UsersContext context;
    private final Function<String, Integer> amountConvert;
    private final Function<LocalDate, String> dateStringConvert;

    public PeriodStatisticCmd(UsersContext context,
                              DateTimeFormatter formatter) {
        this(context, s -> 5, formatter);
    }

    public PeriodStatisticCmd(UsersContext context,
                              Function<String, Integer> amountConvert,
                              DateTimeFormatter formatter) {
        this(context, amountConvert, localDate -> localDate.format(formatter));
    }

    public PeriodStatisticCmd(UsersContext context,
                              Function<String, Integer> amountConvert,
                              Function<LocalDate, String> dateStringConvert) {
        this.context = context;
        this.amountConvert = amountConvert;
        this.dateStringConvert = dateStringConvert;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        return Optional.of(
            new SendText(
                context.callback(
                    users ->

                        new Periods.SmartPeriods(users.user(update.getMessage().getFrom().getId())
                            .periods()).last(amountConvert.apply(update.getMessage().getText()))
                            .stream()
                            .map(p -> dateStringConvert.apply(p.start()) + " - " + p.days().toString())
                            .collect(Collectors.joining("\n"))

                ), update));
    }
}
