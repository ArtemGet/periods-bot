package aget.periodsbot.bot.command;

import aget.periodsbot.bot.convert.Convert;
import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.domain.usecase.FunctionUseCase;
import aget.periodsbot.dto.PeriodAddDto;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Date;
import java.util.Optional;

public class PeriodAddCmd implements Cmd<Update, AbsSender> {
    private final FunctionUseCase<PeriodAddDto, Date> periodAdd;
    private final Convert<Update, PeriodAddDto> periodAddConvert;
    private final Convert<Date, String> dateRsConvert;

    public PeriodAddCmd(FunctionUseCase<PeriodAddDto, Date> periodAdd,
                        Convert<Update, PeriodAddDto> periodAddConvert,
                        Convert<Date, String> dateRsConvert) {
        this.periodAdd = periodAdd;
        this.periodAddConvert = periodAddConvert;
        this.dateRsConvert = dateRsConvert;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        return Optional.of(new SendText(
            new SendMessage(
                update.getMessage().getFrom().getId().toString(),
                this.dateRsConvert.convert(
                    this.periodAdd.handle(
                        this.periodAddConvert.convert(update)
                    )
                )
            )
        ));
    }
}
