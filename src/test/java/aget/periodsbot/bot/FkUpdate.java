package aget.periodsbot.bot;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class FkUpdate {
    private final String username;
    private final Long id;
    private final String message;

    public FkUpdate() {
        this("text");
    }

    public FkUpdate(String message) {
        this("test", 1L, message);
    }

    public FkUpdate(String username, Long id, String message) {
        this.username = username;
        this.id = id;
        this.message = message;
    }

    public Update update() {
        User user = new User();
        user.setUserName(this.username);
        user.setId(this.id);
        Message message = new Message();
        message.setFrom(user);
        message.setText(this.message);
        Update update = new Update();
        update.setMessage(message);
        return update;
    }
}
