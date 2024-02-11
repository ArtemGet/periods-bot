package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.Send;
import aget.periodsbot.bot.send.SendText;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class NoSuchCommand implements Command<Message, Send> {
    @Override
    public Send execute(Message message) {
        return new SendText(
                new SendMessage(
                        message.getFrom().getId().toString(),
                        "Введена несуществующая комманда"));
    }
}
