package aget.periodsbot.repo;

import aget.periodsbot.domain.User;

public interface Users {
    User add(Long usTId, String name);

    User user(Long usTId);
}
