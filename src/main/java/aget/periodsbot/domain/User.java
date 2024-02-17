package aget.periodsbot.domain;

import aget.periodsbot.repo.Periods;

import java.util.UUID;

public interface User {
    UUID id();

    String name();

    Periods periods();
}
