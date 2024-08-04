package aget.periodsbot.bot.update;

import com.github.artemget.teleroute.update.Wrap;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public final class TgBotWrap implements Wrap<Update> {
    private final Update update;

    public TgBotWrap(final Update update) {
        this.update = update;
    }

    @Override
    public Integer identity() {
        return this.update.getUpdateId();
    }

    @Override
    public Boolean isCommand() {
        return Optional.ofNullable(this.update.getMessage())
            .map(Message::isCommand)
            .orElse(false);
    }

    @Override
    public Optional<String> text() {
        return Optional.ofNullable(this.update.getMessage())
            .map(Message::getText);
    }

    @Override
    public Update src() {
        return this.update;
    }
}
