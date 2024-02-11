package aget.periodsbot.dto;

public class UserGreetRqDto {
    private final UserTIdDto userTelegramIdDto;
    private final String name;

    public UserGreetRqDto(UserTIdDto userTelegramIdDto, String name) {
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
