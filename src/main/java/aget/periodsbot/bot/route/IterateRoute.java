package aget.periodsbot.bot.route;

import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class IterateRoute implements Route<Update, Send> {
    private final Collection<Route<Update, Send>> routes;

    public IterateRoute() {
        this(Collections.emptyList());
    }

    @SafeVarargs
    public IterateRoute(final Route<Update, Send>... route) {
        this(Arrays.asList(route));
    }

    public IterateRoute(final Collection<Route<Update, Send>> routes) {
        this.routes = Collections.unmodifiableCollection(routes);
    }

    @Override
    public Optional<Send> route(Update update) {
        return Optional.ofNullable(update)
                .flatMap(
                        upd -> routes.stream()
                                .map(route -> route.route(upd))
                                .filter(Optional::isPresent)
                                .findFirst()
                                .orElse(Optional.empty())
                );
    }
}
