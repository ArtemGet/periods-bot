package aget.periodsbot.bot.route;

import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class TgCmdRoute implements Route<Update, Send> {
    private final Route<Update, Send> route;

    public TgCmdRoute() {
        this(new EndRoute());
    }

    public TgCmdRoute(Route<Update, Send> route) {
        this.route = route;
    }

    @Override
    public Optional<Send> route(Update update) {
        if (!update.hasMessage() || !update.getMessage().isCommand()) {
            return Optional.empty();
        }

        return this.route.route(update);
    }
}
