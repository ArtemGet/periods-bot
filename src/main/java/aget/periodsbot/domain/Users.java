package aget.periodsbot.domain;

public interface Users {
    void add(Long usTId, String name);

    User user(Long usTId);
}
