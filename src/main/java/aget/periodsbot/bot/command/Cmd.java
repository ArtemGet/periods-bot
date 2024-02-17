package aget.periodsbot.bot.command;

public interface Cmd<Req, Resp>{
    Resp execute(Req req);
}
