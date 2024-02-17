package aget.periodsbot.bot.command;

import aget.periodsbot.bot.convert.Convert;
import aget.periodsbot.bot.send.Send;
import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.domain.usecase.FunctionUseCase;
import aget.periodsbot.dto.PeriodsStatsDto;
import aget.periodsbot.dto.UserTIdDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

public class LastPeriodsStatsCmd implements Cmd<Message, Send> {
    private final FunctionUseCase<UserTIdDto, Optional<PeriodsStatsDto>> lastPeriodsStats;
    private final Convert<Message, UserTIdDto> userTIdConvert;
    private final Convert<Optional<PeriodsStatsDto>, String> periodsStatsConvert;

    public LastPeriodsStatsCmd(FunctionUseCase<UserTIdDto, Optional<PeriodsStatsDto>> lastPeriodsStats,
                               Convert<Message, UserTIdDto> userTIdConvert,
                               Convert<Optional<PeriodsStatsDto>, String> periodsStatsConvert) {
        this.lastPeriodsStats = lastPeriodsStats;
        this.userTIdConvert = userTIdConvert;
        this.periodsStatsConvert = periodsStatsConvert;
    }

    @Override
    public Send execute(Message message) {
        return new SendText(
                new SendMessage(
                        message.getFrom().getId().toString(),
                        this.periodsStatsConvert.convert(
                                this.lastPeriodsStats.handle(
                                        this.userTIdConvert.convert(message)
                                )
                        )
                )
        );
    }
}
