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

public class LastPeriodsStatsCommand extends TextCommand {
    private final FunctionUseCase<UserTIdDto, Optional<PeriodsStatsDto>> lastPeriodsStats;
    private final Convert<Message, UserTIdDto> lastPeriodsReqConvert;
    private final Convert<Optional<PeriodsStatsDto>, String> lastPeriodsRespConvert;

    public LastPeriodsStatsCommand(String trigger,
                                   FunctionUseCase<UserTIdDto, Optional<PeriodsStatsDto>> lastPeriodsStats,
                                   Convert<Message, UserTIdDto> lastPeriodsReqConvert,
                                   Convert<Optional<PeriodsStatsDto>, String> lastPeriodsRespConvert) {
        super(trigger);
        this.lastPeriodsStats = lastPeriodsStats;
        this.lastPeriodsReqConvert = lastPeriodsReqConvert;
        this.lastPeriodsRespConvert = lastPeriodsRespConvert;
    }

    @Override
    public Send execute(Message message) {
        return new SendText(
                new SendMessage(
                        message.getFrom().getId().toString(),
                        this.lastPeriodsRespConvert.convert(
                                this.lastPeriodsStats.handle(
                                        this.lastPeriodsReqConvert.convert(message)
                                )
                        )
                )
        );
    }
}
