package aget.periodsbot.bot.command;

public interface Command<Req, Resp>{
    Resp execute(Req req);
}
