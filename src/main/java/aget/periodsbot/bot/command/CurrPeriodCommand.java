package aget.periodsbot.bot.command;

import aget.periodsbot.bot.convert.Convert;
import aget.periodsbot.bot.send.Send;
import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.domain.usecase.FunctionUseCase;
import aget.periodsbot.dto.LastPeriodStatsDto;
import aget.periodsbot.dto.UserTIdDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class CurrPeriodCommand extends TextCommand {
    private final FunctionUseCase<UserTIdDto, LastPeriodStatsDto> currPeriod;
    private final Convert<Message, UserTIdDto> userTIdConvert;
    private final Convert<LastPeriodStatsDto, String> lastPeriodStatsConvert;

    public CurrPeriodCommand(String trigger,
                             FunctionUseCase<UserTIdDto, LastPeriodStatsDto> currPeriod,
                             Convert<Message, UserTIdDto> userTIdConvert,
                             Convert<LastPeriodStatsDto, String> lastPeriodStatsConvert) {
        super(trigger);
        this.currPeriod = currPeriod;
        this.userTIdConvert = userTIdConvert;
        this.lastPeriodStatsConvert = lastPeriodStatsConvert;
    }

    @Override
    public Send execute(Message message) {
        return new SendText(
                new SendMessage(
                        message.getFrom().getId().toString(),
                        this.lastPeriodStatsConvert.convert(
                                this.currPeriod.handle(
                                        this.userTIdConvert.convert(message)
                                )
                        )
                )
        );
    }
}
