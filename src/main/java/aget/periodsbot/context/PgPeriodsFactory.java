package aget.periodsbot.context;

import aget.periodsbot.domain.Periods;
import aget.periodsbot.domain.PgPeriods;
import org.jdbi.v3.core.Handle;

import java.util.UUID;

public class PgPeriodsFactory implements PeriodsFactory {
    @Override
    public Periods periods(Handle handle, UUID usId) {
        return new PgPeriods(handle, usId);
    }
}
