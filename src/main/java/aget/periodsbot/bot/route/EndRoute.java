package aget.periodsbot.bot.route;

import aget.periodsbot.bot.command.Cmd;
import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class EndRoute implements Route<Update, Send> {
    //TODO: add SkipCmd and DontSend instead using Optional as class attribute
    private final Optional<Cmd<Message, Send>> deadEndCommand;

    public EndRoute() {
        this.deadEndCommand = Optional.empty();
    }

    public EndRoute(final Cmd<Message, Send> deadEndCmd) {
        this.deadEndCommand = Optional.ofNullable(deadEndCmd);
    }

    @Override
    public Optional<Send> route(Update update) {
        return this.deadEndCommand.map(
                command -> command.execute(update.getMessage())
        );
    }
}
