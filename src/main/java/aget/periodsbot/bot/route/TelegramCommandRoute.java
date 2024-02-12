package aget.periodsbot.bot.route;

import aget.periodsbot.bot.command.Command;
import aget.periodsbot.bot.command.TextCommand;
import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TelegramCommandRoute implements Route<Update, Send> {
    private final Route<Update, Send> route;

    public TelegramCommandRoute() {
        this(Collections.emptyMap());
    }

    public TelegramCommandRoute(Route<Update, Send> route) {
        this.route = route;
    }

    public TelegramCommandRoute(Map<String, Command<Message, Send>> commandMap) {
        this.route = new TextCommandRoute(Collections.unmodifiableMap(commandMap));
    }

    public TelegramCommandRoute(final TextCommand... textCommand) {
        this(Arrays.asList(textCommand));
    }

    public TelegramCommandRoute(Collection<TextCommand> commands) {
        this(commands.stream()
                .collect(Collectors.toMap(TextCommand::trigger, Function.identity())));
    }

    @Override
    public Optional<Send> route(Update update) {
        if (!update.hasMessage() || !update.getMessage().isCommand()) {
            return Optional.empty();
        }

        return this.route.route(update);
    }
}
