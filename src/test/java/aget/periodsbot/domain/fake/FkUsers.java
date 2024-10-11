package aget.periodsbot.domain.fake;

import aget.periodsbot.domain.User;
import aget.periodsbot.domain.Users;

import java.util.Dictionary;
import java.util.Hashtable;

public class FkUsers implements Users {
    private final Dictionary<Long, User> users;

    public FkUsers() {
        this(new Hashtable<>());
    }

    public FkUsers(Dictionary<Long, User> users) {
        this.users = users;
    }

    @Override
    public void add(Long usTId, String name) {
        users.put(usTId, new FkUser(usTId, name, new FkPeriods()));
    }

    @Override
    public User user(Long usTId) {
        return users.get(usTId);
    }
}
