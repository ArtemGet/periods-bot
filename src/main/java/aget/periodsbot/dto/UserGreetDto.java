package aget.periodsbot.dto;

public class UserGreetDto {
    private final UserTIdDto userTelegramIdDto;
    private final String name;

    public UserGreetDto(UserTIdDto userTelegramIdDto, String name) {
        this.userTelegramIdDto = userTelegramIdDto;
        this.name = name;
    }

    public UserTIdDto userTelegramIdDto() {
        return this.userTelegramIdDto;
    }

    public String name() {
        return this.name;
    }
}
