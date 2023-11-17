package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.attempt.crispr_allele_modification.CrisprAlleleModificationAttempt;
import org.gentar.biology.plan.engine.breeding.BreedingPlanEvent;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanEvent;
import org.gentar.biology.plan.engine.crispr.HaploessentialProductionPlanEvent;
import org.gentar.biology.plan.engine.crispr_allele_modification.CrisprAlleleModificationPlanEvent;
import org.gentar.biology.plan.engine.es_cell.EsCellProductionPlanEvent;
import org.gentar.biology.plan.engine.es_cell_allele_modification.EsCellAlleleModificationPlanEvent;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.test_util.PlanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PlanStateMachineResolverTest
{
    private PlanStateMachineResolver testInstance;

    @BeforeEach
    public void setup()
    {
        testInstance = new PlanStateMachineResolver();
    }

    @Test
    public void testWhenCrispr()
    {
        Plan plan = PlanBuilder.getInstance()
            .withPlanType(PlanTypeName.PRODUCTION.getLabel())
            .withAttemptType(AttemptTypesName.CRISPR.getLabel())
            .build();
        ProcessEvent processEvent =
            testInstance.getProcessEventByActionName(
                plan, CrisprProductionPlanEvent.abandonWhenCreated.getName());
        assertThat(
            "Invalid state machine",
            (processEvent instanceof CrisprProductionPlanEvent),
            is(true));
        assertThat(
            "Invalid event",
            processEvent.getName(),
            is(CrisprProductionPlanEvent.abandonWhenCreated.getName()));
    }


    @Test
    public void testWhenHaploEssentialCrispr()
    {
        Plan plan = PlanBuilder.getInstance()
            .withPlanType(PlanTypeName.PRODUCTION.getLabel())
            .withAttemptType(AttemptTypesName.HAPLOESSENTIAL_CRISPR.getLabel())
            .build();
        ProcessEvent processEvent =
            testInstance.getProcessEventByActionName(
                plan, HaploessentialProductionPlanEvent.abandonWhenCreated.getName());
        assertThat(
            "Invalid state machine",
            (processEvent instanceof HaploessentialProductionPlanEvent),
            is(true));
        assertThat(
            "Invalid event",
            processEvent.getName(),
            is(HaploessentialProductionPlanEvent.abandonWhenCreated.getName()));
    }

    @Test
    public void testWhenBreeding()
    {
        Plan plan = PlanBuilder.getInstance()
            .withPlanType(PlanTypeName.PRODUCTION.getLabel())
            .withAttemptType(AttemptTypesName.BREEDING.getLabel())
            .build();
        ProcessEvent processEvent =
            testInstance.getProcessEventByActionName(
                plan, BreedingPlanEvent.abandonWhenCreated.getName());
        assertThat(
            "Invalid state machine",
            (processEvent instanceof BreedingPlanEvent),
            is(true));
        assertThat(
            "Invalid event",
            processEvent.getName(),
            is(BreedingPlanEvent.abandonWhenCreated.getName()));
    }

    @Test
    public void testWhenEsCEllModification()
    {
        Plan plan = PlanBuilder.getInstance()
            .withPlanType(PlanTypeName.PRODUCTION.getLabel())
            .withAttemptType(AttemptTypesName.ES_CELL_ALLELE_MODIFICATION.getLabel())
            .build();
        ProcessEvent processEvent =
            testInstance.getProcessEventByActionName(
                plan, EsCellAlleleModificationPlanEvent.abandonWhenCreated.getName());
        assertThat(
            "Invalid state machine",
            (processEvent instanceof EsCellAlleleModificationPlanEvent),
            is(true));
        assertThat(
            "Invalid event",
            processEvent.getName(),
            is(EsCellAlleleModificationPlanEvent.abandonWhenCreated.getName()));
    }

    @Test
    public void testWhenCrisprModification()
    {
        Plan plan = PlanBuilder.getInstance()
            .withPlanType(PlanTypeName.PRODUCTION.getLabel())
            .withAttemptType(AttemptTypesName.CRISPR_ALLELE_MODIFICATION.getLabel())
            .build();
        ProcessEvent processEvent =
            testInstance.getProcessEventByActionName(
                plan, CrisprAlleleModificationPlanEvent.abandonWhenCreated.getName());
        assertThat(
            "Invalid state machine",
            (processEvent instanceof CrisprAlleleModificationPlanEvent),
            is(true));
        assertThat(
            "Invalid event",
            processEvent.getName(),
            is(CrisprAlleleModificationPlanEvent.abandonWhenCreated.getName()));
    }

    @Test
    public void testWhenEsCEll()
    {
        Plan plan = PlanBuilder.getInstance()
            .withPlanType(PlanTypeName.PRODUCTION.getLabel())
            .withAttemptType(AttemptTypesName.ES_CELL.getLabel())
            .build();
        ProcessEvent processEvent =
            testInstance.getProcessEventByActionName(
                plan, EsCellProductionPlanEvent.abandonWhenCreated.getName());
        assertThat(
            "Invalid state machine",
            (processEvent instanceof EsCellProductionPlanEvent),
            is(true));
        assertThat(
            "Invalid event",
            processEvent.getName(),
            is(EsCellProductionPlanEvent.abandonWhenCreated.getName()));
    }

    @Test
    public void testWhenAdultPhenotyping()
    {
        Plan plan = PlanBuilder.getInstance()
            .withPlanType(PlanTypeName.PRODUCTION.getLabel())
            .withAttemptType(AttemptTypesName.ADULT_PHENOTYPING.getLabel())
            .build();
        ProcessEvent processEvent =
            testInstance.getProcessEventByActionName(
                plan, EsCellProductionPlanEvent.abandonWhenCreated.getName());
        assertEquals(processEvent.getName(),EsCellProductionPlanEvent.abandonWhenCreated.getName());
    }

}