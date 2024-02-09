package aget.periodsbot.domain;

import aget.periodsbot.repo.Periods;

public interface User {
    String name();

    Periods periods();
}
