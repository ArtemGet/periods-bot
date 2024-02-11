package aget.periodsbot.bot.route;

import aget.periodsbot.bot.command.Command;
import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class DeadEndRoute implements Route<Update, Send> {
    private final Optional<Command<Message, Send>> deadEndCommand;

    public DeadEndRoute(Command<Message, Send> deadEndCommand) {
        this.deadEndCommand = Optional.ofNullable(deadEndCommand);
    }

    public DeadEndRoute() {
        this.deadEndCommand = Optional.empty();
    }

    @Override
    public Optional<Send> route(Update update) {
        return this.deadEndCommand
                .map(command -> command.execute(update.getMessage()));
    }
}
