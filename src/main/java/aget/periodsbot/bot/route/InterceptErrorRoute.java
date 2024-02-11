package aget.periodsbot.bot.route;

import aget.periodsbot.bot.send.Send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class InterceptErrorRoute implements Route<Update, Send> {
    private static final Logger log = LoggerFactory.getLogger(InterceptErrorRoute.class);
    private final Route<Update, Send> route;
    private final Route<Update, Send> errorRoute;

    public InterceptErrorRoute(Route<Update, Send> route) {
        this.route = route;
        this.errorRoute = new DeadEndRoute();
    }

    public InterceptErrorRoute(Route<Update, Send> route,
                               Route<Update, Send> errorRoute) {
        this.route = route;
        this.errorRoute = errorRoute;
    }

    @Override
    public Optional<Send> route(Update update) {
        try {
            return this.route.route(update);
        } catch (Exception e) {
            log.error("Error occurred while executing command: {}", e.getMessage(), e);

            return this.errorRoute.route(update);
        }
    }
}
