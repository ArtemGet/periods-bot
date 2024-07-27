package aget.periodsbot.domain;

public interface Users {
    User add(Long usTId, String name);

    User user(Long usTId);
}
