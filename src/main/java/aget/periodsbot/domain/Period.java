package aget.periodsbot.domain;

import java.time.LocalDate;

public interface Period {
    LocalDate start();
    Integer days();
}
