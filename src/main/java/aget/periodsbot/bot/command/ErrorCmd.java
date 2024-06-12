package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.SendText;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

public class ErrorCmd implements Cmd<Update, AbsSender> {
    @Override
    public Optional<Send<AbsSender>> execute(Update update) {
        return Optional.of(new SendText(
            new SendMessage(
                update.getMessage().getFrom().getId().toString(),
                "Ошибка обработки запроса"
            )
        ));
    }
}
