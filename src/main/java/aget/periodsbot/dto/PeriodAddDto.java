package aget.periodsbot.dto;

import java.time.LocalDate;

public class PeriodAddDto {
    private final UserTIdDto userTIdDto;
    private final LocalDate cycleStartDate;

    public PeriodAddDto(UserTIdDto userTIdDto, LocalDate cycleStartDate) {
        this.userTIdDto = userTIdDto;
        this.cycleStartDate = cycleStartDate;
    }

    public UserTIdDto userTelegramIdDto() {
        return this.userTIdDto;
    }

    public LocalDate cycleStartDate() {
        return this.cycleStartDate;
    }
}
