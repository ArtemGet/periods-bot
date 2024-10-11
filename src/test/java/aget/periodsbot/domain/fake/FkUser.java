package aget.periodsbot.domain.fake;

import aget.periodsbot.domain.Periods;
import aget.periodsbot.domain.User;

import java.util.Objects;

public class FkUser implements User {
    private final Long id;
    private final String name;
    private final Periods periods;

    public FkUser(Long id, String name, Periods periods) {
        this.id = id;
        this.name = name;
        this.periods = periods;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Periods periods() {
        return this.periods;
    }

    @Override
    public boolean equals(Object object) {
        return this == object ||
            object instanceof FkUser
                && this.id.equals(((FkUser) object).id)
                && this.name.equals(((FkUser) object).name)
                && this.periods.equals(((FkUser) object).periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.periods);
    }
}
