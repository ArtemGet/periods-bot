package aget.periodsbot.bot.route;

import java.util.Optional;

public interface Route<Req, Resp>{
    Optional<Resp> route(Req req);
}
