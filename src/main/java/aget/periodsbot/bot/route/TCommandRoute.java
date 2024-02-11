package aget.periodsbot.bot.route;

import aget.periodsbot.bot.command.Command;
import aget.periodsbot.bot.command.TextCommand;
import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TCommandRoute implements Route<Message, Send> {
    private final Route<Message, Send> route;

    public TCommandRoute() {
        this(Collections.emptyMap());
    }

    public TCommandRoute(Route<Message, Send> route) {
        this.route = route;
    }

    public TCommandRoute(Map<String, Command<Message, Send>> commandMap) {
        this.route = new TextRoute(Collections.unmodifiableMap(commandMap));
    }

    public TCommandRoute(final TextCommand... textCommand) {
        this(Arrays.asList(textCommand));
    }

    public TCommandRoute(Collection<TextCommand> commands) {
        this(commands.stream()
                .collect(Collectors.toMap(TextCommand::trigger, Function.identity())));
    }

    @Override
    public Optional<Send> route(Message message) {
        if (!message.isCommand()) {
            return Optional.empty();
        }

        return this.route.route(message);
    }
}
