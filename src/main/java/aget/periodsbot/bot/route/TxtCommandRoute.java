package aget.periodsbot.bot.route;

import aget.periodsbot.bot.command.Command;
import aget.periodsbot.bot.command.TextCommand;
import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TxtCommandRoute implements Route<Update, Send> {
    private final Map<String, Command<Message, Send>> commandMap;

    public TxtCommandRoute(Map<String, Command<Message, Send>> commandMap) {
        this.commandMap = Collections.unmodifiableMap(commandMap);
    }

    public TxtCommandRoute(TextCommand... textCommand) {
        this(Arrays.asList(textCommand));
    }

    public TxtCommandRoute(List<TextCommand> commands) {
        this(commands.stream()
                .collect(Collectors.toMap(TextCommand::trigger, Function.identity())));
    }

    @Override
    public Optional<Send> route(Update update) {
        return Optional.ofNullable(
                this.commandMap.get(update.getMessage().getText())
        ).map(
                command -> command.execute(update.getMessage())
        );
    }
}
