package aget.periodsbot.bot.command;

import aget.periodsbot.bot.convert.Convert;
import aget.periodsbot.bot.send.SendText;
import aget.periodsbot.domain.usecase.FunctionUseCase;
import aget.periodsbot.dto.LastPeriodStatsDto;
import aget.periodsbot.dto.UserTIdDto;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

public class CurrPeriodCmd implements Cmd<Update, AbsSender> {
    private final FunctionUseCase<UserTIdDto, LastPeriodStatsDto> currPeriod;
    private final Convert<Update, UserTIdDto> userTIdConvert;
    private final Convert<LastPeriodStatsDto, String> lastPeriodStatsConvert;

    public CurrPeriodCmd(FunctionUseCase<UserTIdDto, LastPeriodStatsDto> currPeriod,
                         Convert<Update, UserTIdDto> userTIdConvert,
                         Convert<LastPeriodStatsDto, String> lastPeriodStatsConvert) {
        this.currPeriod = currPeriod;
        this.userTIdConvert = userTIdConvert;
        this.lastPeriodStatsConvert = lastPeriodStatsConvert;
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        return Optional.of(new SendText(
            new SendMessage(
                update.getMessage().getFrom().getId().toString(),
                this.lastPeriodStatsConvert.convert(
                    this.currPeriod.handle(
                        this.userTIdConvert.convert(update)
                    )
                )
            )
        ));
    }
}
