package aget.periodsbot.bot.command.trigger;

import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.command.CmdException;
import com.github.artemget.teleroute.send.Send;

import java.util.Optional;

public class TriggerCmd<T, Req, Resp> implements Trigger<T>, Cmd<Req, Resp> {
    private final Cmd<Req, Resp> cmd;
    private final T trigger;

    public TriggerCmd(T trigger, Cmd<Req, Resp> cmd) {
        this.cmd = cmd;
        this.trigger = trigger;
    }

    @Override
    public Optional<Send<Resp>> execute(Req req) throws CmdException {
        return this.cmd.execute(req);
    }

    @Override
    public T trigger() {
        return trigger;
    }
}
