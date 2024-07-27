package aget.periodsbot.context;

import aget.periodsbot.domain.Periods;
import org.jdbi.v3.core.Handle;

import java.util.UUID;

public interface PeriodsFactory {
    Periods periods(Handle handle, UUID usId);
}
