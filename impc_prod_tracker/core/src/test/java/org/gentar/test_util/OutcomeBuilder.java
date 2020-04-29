package org.gentar.test_util;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.status.Status;

public class OutcomeBuilder
{
    private Long id;
    private OutcomeTypeName outcomeTypeName;
    private OutcomeType outcomeType;
    private Colony colony;
    private boolean isAbortionStatusColony;
    private int statusOrder;

    public static OutcomeBuilder getInstance()
    {
        return new OutcomeBuilder();
    }

    public Outcome build()
    {
        Outcome outcome = new Outcome();
        outcome.setId(id);
        outcome.setOutcomeType(outcomeType);
        if (colony != null)
        {
            colony.setStatus(buildStatus(isAbortionStatusColony, statusOrder));
            outcome.setColony(colony);
            colony.setOutcome(outcome);
        }
        return outcome;
    }

    public OutcomeBuilder withId(Long id)
    {
        this.id = id;
        return this;
    }

    private OutcomeBuilder withOutcomeTypeName(OutcomeTypeName outcomeTypeName)
    {
        this.outcomeTypeName = outcomeTypeName;
        return this;
    }

    public OutcomeBuilder withColony()
    {
        this.colony = new Colony();
        this.colony.setId(this.id);
        this.outcomeType = buildOutcomeType(OutcomeTypeName.COLONY);
        return this;
    }

    public OutcomeBuilder withIsAbortionStatusColony(boolean isAbortionStatusColony)
    {
        this.isAbortionStatusColony = isAbortionStatusColony;
        return this;
    }

    public OutcomeBuilder withStatusOrder(int order)
    {
        this.statusOrder = order;
        return this;
    }

    private OutcomeType buildOutcomeType(OutcomeTypeName typeName)
    {
        OutcomeType outcomeType = new OutcomeType();
        if (typeName != null)
        {
            outcomeType.setName(typeName.getLabel());
        }
        return outcomeType;
    }

    private Status buildStatus(boolean isAbortionStatus, int statusOrder)
    {
        Status status = new Status();
        status.setIsAbortionStatus(isAbortionStatus);
        status.setOrdering(statusOrder);
        return status;
    }
}
