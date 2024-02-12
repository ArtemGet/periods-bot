package aget.periodsbot.bot.command;

import aget.periodsbot.bot.convert.Convert;
import aget.periodsbot.bot.send.Send;
import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.domain.usecase.FunctionUseCase;
import aget.periodsbot.dto.PeriodAddDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Date;

public class PeriodAddCommand extends TextCommand {
    private final FunctionUseCase<PeriodAddDto, Date> periodAdd;
    private final Convert<Message, PeriodAddDto> periodAddConvert;
    private final Convert<Date, String> dateRsConvert;

    public PeriodAddCommand(String trigger,
                            FunctionUseCase<PeriodAddDto, Date> periodAdd,
                            Convert<Message, PeriodAddDto> periodAddConvert,
                            Convert<Date, String> dateRsConvert) {
        super(trigger);
        this.periodAdd = periodAdd;
        this.periodAddConvert = periodAddConvert;
        this.dateRsConvert = dateRsConvert;
    }

    @Override
    public Send execute(Message message) {
        return new SendText(
                new SendMessage(
                        message.getFrom().getId().toString(),
                        this.dateRsConvert.convert(
                                this.periodAdd.handle(
                                        this.periodAddConvert.convert(message)
                                )
                        )
                )
        );
    }
}
