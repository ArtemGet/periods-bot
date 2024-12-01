package aget.periodsbot.bot.command;

import aget.periodsbot.bot.send.KeyboardTgSend;
import aget.periodsbot.bot.send.SendMsg;
import com.github.artemget.teleroute.command.Cmd;
import com.github.artemget.teleroute.send.Send;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class KeyboardCmd implements Cmd<Update, AbsSender> {
    private final ReplyKeyboardMarkup keyboardMarkup;

    public KeyboardCmd(String... buttons) {
        this(new KeyboardRow(Arrays.stream(buttons).map(KeyboardButton::new).toList()));
    }

    public KeyboardCmd(KeyboardRow row) {
        this(new ReplyKeyboardMarkup(Collections.singletonList(row)));
    }

    public KeyboardCmd(ReplyKeyboardMarkup keyboardMarkup) {
        this.keyboardMarkup = keyboardMarkup;
        keyboardMarkup.setResizeKeyboard(true);
    }

    @Override
    public Optional<Send<AbsSender>> execute(Update update){
        return Optional.of(
            new SendMsg(
                new KeyboardTgSend(
                    update.getMessage().getFrom().getId().toString(),
                    "Выберите действие на клавиатуре",
                    keyboardMarkup
                )
            )
        );
    }
}
