package aget.periodsbot.dto;

import java.util.Date;

public class PeriodAddDto {
    private final UserTIdDto userTIdDto;
    private final Date cycleStartDate;

    public PeriodAddDto(UserTIdDto userTIdDto, Date cycleStartDate) {
        this.userTIdDto = userTIdDto;
        this.cycleStartDate = cycleStartDate;
    }

    public UserTIdDto userTelegramIdDto() {
        return this.userTIdDto;
    }

    public Date cycleStartDate() {
        return this.cycleStartDate;
    }
}
