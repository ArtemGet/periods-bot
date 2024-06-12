package aget.periodsbot.bot.route;

import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.route.Route;
import com.github.artemget.teleroute.route.RouteEnd;
import com.github.artemget.teleroute.update.Wrap;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

public class SecureRoute implements Route<Update, AbsSender> {
    private final Route<Update, AbsSender> route;

    public SecureRoute() {
        this(new RouteEnd<>());
    }

    public SecureRoute(final Route<Update, AbsSender> route) {
        this.route = route;
    }

    @Override
    public Optional<Cmd<Update, AbsSender>> route(Wrap<Update> update) {
        return this.route.route(update);
    }
}
