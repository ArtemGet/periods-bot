package aget.periodsbot.dto;

public class UserTIdDto {
    private final Long userTelegramId;

    public UserTIdDto(Long userTelegramId) {
        this.userTelegramId = userTelegramId;
    }

    public Long userTelegramId() {
        return userTelegramId;
    }
}
