package aget.periodsbot.domain;

import org.jdbi.v3.core.Handle;

public interface UsersFactory {
    Users provide(Handle ds);
}
