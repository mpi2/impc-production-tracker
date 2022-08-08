package org.gentar.test_util;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.status.Status;

public class PlanBuilder
{
    private Long id;
    private PlanType planType;
    private AttemptType attemptType;
    private Status status;
    private boolean isStatusAbortion;
    private int statusOrder;
    private Status summaryStatus;
    private String pin;

    public static PlanBuilder getInstance()
    {
        return new PlanBuilder();
    }

    public Plan build()
    {
        Plan plan = new Plan();
        plan.setId(id);
        plan.setPlanType(planType);
        plan.setAttemptType(attemptType);
        plan.setPin(pin);
        if (status != null)
        {
            status.setIsAbortionStatus(isStatusAbortion);
            status.setOrdering(statusOrder);
        }
        plan.setStatus(status);
        plan.setSummaryStatus(summaryStatus);
        return plan;
    }

    public PlanBuilder withId(Long id)
    {
        this.id = id;
        return this;
    }

    public PlanBuilder withPin(String pin)
    {
        this.pin = pin;
        return this;
    }

    public PlanBuilder withPlanType(String typeName)
    {
        PlanType planType = new PlanType();
        planType.setName(typeName);
        this.planType = planType;
        return this;
    }

    public PlanBuilder withAttemptType(String attemptTypeName)
    {
        AttemptType attemptType = new AttemptType();
        attemptType.setName(attemptTypeName);
        this.attemptType = attemptType;
        return this;
    }

    public PlanBuilder withStatus(String statusName)
    {
        Status status = new Status();
        status.setName(statusName);
        this.status = status;
        return this;
    }

    public PlanBuilder withStatusAndOrder(String statusName, int order)
    {
        Status status = new Status();
        status.setName(statusName);
        this.status = status;
        this.statusOrder = order;
        return this;
    }

    public PlanBuilder withIsStatusAbortion(boolean isStatusAbortion)
    {
        this.isStatusAbortion = isStatusAbortion;
        return this;
    }

    public PlanBuilder withSummaryStatus(String summaryStatusName)
    {
        Status status = new Status();
        status.setName(summaryStatusName);
        this.summaryStatus = status;
        return this;
    }
}
