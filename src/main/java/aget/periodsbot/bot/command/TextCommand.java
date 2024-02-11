package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.Send;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class TextCommand implements Command<Message, Send>{
    private final String trigger;

    public TextCommand(String trigger){
        this.trigger = trigger;
    }

    public String trigger(){
        return this.trigger;
    }
}
