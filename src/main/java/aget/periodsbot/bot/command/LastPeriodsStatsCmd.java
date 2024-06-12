package aget.periodsbot.bot.command;

import aget.periodsbot.bot.convert.Convert;
import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.domain.usecase.FunctionUseCase;
import aget.periodsbot.dto.PeriodsStatsDto;
import aget.periodsbot.dto.UserTIdDto;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

public class LastPeriodsStatsCmd implements Cmd<Update, AbsSender> {
    private final FunctionUseCase<UserTIdDto, Optional<PeriodsStatsDto>> lastPeriodsStats;
    private final Convert<Update, UserTIdDto> userTIdConvert;
    private final Convert<Optional<PeriodsStatsDto>, String> periodsStatsConvert;

    public LastPeriodsStatsCmd(FunctionUseCase<UserTIdDto, Optional<PeriodsStatsDto>> lastPeriodsStats,
                               Convert<Update, UserTIdDto> userTIdConvert,
                               Convert<Optional<PeriodsStatsDto>, String> periodsStatsConvert) {
        this.lastPeriodsStats = lastPeriodsStats;
        this.userTIdConvert = userTIdConvert;
        this.periodsStatsConvert = periodsStatsConvert;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        return Optional.of(new SendText(
            new SendMessage(
                update.getMessage().getFrom().getId().toString(),
                this.periodsStatsConvert.convert(
                    this.lastPeriodsStats.handle(
                        this.userTIdConvert.convert(update)
                    )
                )
            )
        ));
    }
}
