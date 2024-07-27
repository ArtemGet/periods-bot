package aget.periodsbot.domain;

import aget.periodsbot.domain.User;

public interface Users {
    void add(Long usTId, String name);

    User user(Long usTId);
}
