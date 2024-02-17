package aget.periodsbot.bot.route;

import aget.periodsbot.bot.command.Cmd;
import aget.periodsbot.bot.command.trigger.TriggerCmd;
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

public class TxtCmdRoute implements Route<Update, Send> {
    private final Map<String, Cmd<Message, Send>> commandMap;

    public TxtCmdRoute() {
        this(Collections.emptyMap());
    }

    @SafeVarargs
    public TxtCmdRoute(final TriggerCmd<String, Message, Send>... txtCmd) {
        this(Arrays.asList(txtCmd));
    }

    public TxtCmdRoute(final Collection<TriggerCmd<String, Message, Send>> txtCmd) {
        this(txtCmd.stream()
                .collect(Collectors.toMap(
                                TriggerCmd::trigger,
                                Function.identity()
                        )
                )
        );
    }

    public TxtCmdRoute(final Map<String, Cmd<Message, Send>> txtCmdMap) {
        this.commandMap = Collections.unmodifiableMap(txtCmdMap);
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
