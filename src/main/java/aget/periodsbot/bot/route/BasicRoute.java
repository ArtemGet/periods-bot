package aget.periodsbot.bot.route;

import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class BasicRoute implements Route<Update, Send> {
    private final Collection<Route<Update, Send>> routes;

    public BasicRoute() {
        this(Collections.emptyList());
    }

    public BasicRoute(Collection<Route<Update, Send>> routes) {
        this.routes = Collections.unmodifiableCollection(routes);
    }

    @SafeVarargs
    public BasicRoute(final Route<Update, Send>... route) {
        this(Arrays.asList(route));
    }

    @Override
    public Optional<Send> route(Update update) {
        return Optional.ofNullable(update)
                .map(
                        upd -> routes.stream()
                                .map(route -> route.route(upd))
                                .filter(Optional::isPresent)
                                .findFirst()
                )
                .map(Optional::get)
                .map(Optional::get);
    }
}
