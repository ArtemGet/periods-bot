package aget.periodsbot.bot.command.trigger;

import aget.periodsbot.bot.command.Cmd;

public class TriggerCmd<T, Req, Resp> implements Trigger<T>, Cmd<Req, Resp> {
    private final Cmd<Req, Resp> cmd;
    private final T trigger;

    public TriggerCmd(T trigger, Cmd<Req, Resp> cmd) {
        this.cmd = cmd;
        this.trigger = trigger;
    }

    @Override
    public Resp execute(Req req) {
        return this.cmd.execute(req);
    }

    @Override
    public T trigger() {
        return trigger;
    }
}
