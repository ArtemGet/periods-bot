package aget.periodsbot.bot.route;

import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class SecureRoute implements Route<Update, Send> {
    private final Route<Update, Send> route;

    public SecureRoute() {
        this(new EndRoute());
    }

    public SecureRoute(final Route<Update, Send> route) {
        this.route = route;
    }

    @Override
    public Optional<Send> route(Update update) {
        //TODO: add security provide
        return this.route.route(update);
    }
}
