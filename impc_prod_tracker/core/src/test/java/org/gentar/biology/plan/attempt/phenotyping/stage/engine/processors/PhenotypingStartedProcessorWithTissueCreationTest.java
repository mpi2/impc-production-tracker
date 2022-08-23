package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageStateSetter;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.PhenotypingStageEvent;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.PhenotypingStageState;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedType;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedTypeNames;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedTypeService;
import org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution.TissueDistribution;
import org.gentar.biology.status.Status;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhenotypingStartedProcessorWithTissueCreationTest
{
    private PhenotypingStartedProcessorWithTissueCreation testInstance;

    @Mock
    private PhenotypingStageStateSetter stageStateSetter;

    @Mock
    private MaterialDepositedTypeService materialDepositedTypeService;

    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;

    private static final String PARAFFIN_EMBEDDED_SECTIONS_NAME =
        MaterialDepositedTypeNames.PARAFFIN_EMBEDDED_SECTIONS.getLabel();
    private static final String FIXED_TISSUE_NAME = MaterialDepositedTypeNames.FIXED_TISSUE.getLabel();

    private final MaterialDepositedType fixedMaterialDepositedType = new MaterialDepositedType();
    private MaterialDepositedType paraffinMaterialDepositedType;

    private static final String AUTOMATIC_TISSUE_CREATION_WORK_UNIT = "UCD";
    private static final String NOT_AUTOMATIC_TISSUE_CREATION_WORK_UNIT = "JAX";

    @BeforeEach
    void setUp()
    {
        testInstance = new PhenotypingStartedProcessorWithTissueCreation(
            stageStateSetter, policyEnforcement, materialDepositedTypeService);
        fixedMaterialDepositedType.setName(FIXED_TISSUE_NAME);
        paraffinMaterialDepositedType = new MaterialDepositedType();
        paraffinMaterialDepositedType.setName(PARAFFIN_EMBEDDED_SECTIONS_NAME);
    }

    @Test
    public void testWhenUserIsAllowedAndNoTissueDistributionAndAutomaticTissue()
    {
        PhenotypingStage phenotypingStage = buildPhenotypingStage(
            PhenotypingStageState.PhenotypingRegistered.getInternalName(),
            AUTOMATIC_TISSUE_CREATION_WORK_UNIT);
        phenotypingStage.setProcessDataEvent(PhenotypingStageEvent.updateToPhenotypingStarted);
        when(policyEnforcement.hasPermission(null, Actions.UPDATE_TO_PHENOTYPING_STARTED)).thenReturn(true);
        when(materialDepositedTypeService.getMaterialDepositedTypeByName(FIXED_TISSUE_NAME))
            .thenReturn(fixedMaterialDepositedType);
        testInstance.process(phenotypingStage);

        assertThat(
            "Expected 1 tissue distribution", phenotypingStage.getTissueDistributions().size(), is(1));
        TissueDistribution tissueDistribution = phenotypingStage.getTissueDistributions().iterator().next();
        assertThat(
            "End date should be null", tissueDistribution.getEndDate(), is(nullValue()));
        assertThat(
            "Unexpected work unit",
            tissueDistribution.getWorkUnit().getName(),
            is(AUTOMATIC_TISSUE_CREATION_WORK_UNIT));
        assertThat(
            "Unexpected material deposited type",
            tissueDistribution.getMaterialDepositedType().getName(),
            is(FIXED_TISSUE_NAME));

        verify(
            stageStateSetter, times(1)).setStatusByName(
            any(PhenotypingStage.class),
            eq(PhenotypingStageState.PhenotypingStarted.getInternalName()));
    }

    @Test
    public void testWhenUserIsAllowedAndFixedTissueDistributionAndAutomaticTissue()
    {
        PhenotypingStage phenotypingStage = buildPhenotypingStage(
            PhenotypingStageState.PhenotypingRegistered.getInternalName(),
            AUTOMATIC_TISSUE_CREATION_WORK_UNIT);
        TissueDistribution existingTissueDistribution = new TissueDistribution();
        existingTissueDistribution.setEndDate(LocalDate.now());
        existingTissueDistribution.setMaterialDepositedType(fixedMaterialDepositedType);
        phenotypingStage.addTissueDistribution(existingTissueDistribution);
        phenotypingStage.setProcessDataEvent(PhenotypingStageEvent.updateToPhenotypingStarted);
        when(policyEnforcement.hasPermission(null, Actions.UPDATE_TO_PHENOTYPING_STARTED)).thenReturn(true);
        testInstance.process(phenotypingStage);

        assertThat(
            "Expected 1 tissue distribution", phenotypingStage.getTissueDistributions().size(), is(1));
        TissueDistribution tissueDistribution = phenotypingStage.getTissueDistributions().iterator().next();
        assertThat(
            "End date should be null", tissueDistribution.getEndDate(), is(nullValue()));
        assertThat(
            "Unexpected material deposited type",
            tissueDistribution.getMaterialDepositedType().getName(),
            is(FIXED_TISSUE_NAME));

        verify(
            stageStateSetter, times(1)).setStatusByName(
            any(PhenotypingStage.class),
            eq(PhenotypingStageState.PhenotypingStarted.getInternalName()));
    }

    @Test
    public void testWhenUserIsAllowedAndNoTissueDistributionAndNotAutomaticTissue()
    {
        PhenotypingStage phenotypingStage = buildPhenotypingStage(
            PhenotypingStageState.PhenotypingRegistered.getInternalName(),
            NOT_AUTOMATIC_TISSUE_CREATION_WORK_UNIT);
        phenotypingStage.setProcessDataEvent(PhenotypingStageEvent.updateToPhenotypingStarted);
        when(policyEnforcement.hasPermission(null, Actions.UPDATE_TO_PHENOTYPING_STARTED)).thenReturn(true);

        testInstance.process(phenotypingStage);

        assertThat(
            "Expected no tissue distribution", phenotypingStage.getTissueDistributions(), is(nullValue()));

        verify(
            stageStateSetter, times(1)).setStatusByName(
            any(PhenotypingStage.class),
            eq(PhenotypingStageState.PhenotypingStarted.getInternalName()));
    }

    @Test
    public void testWhenUserIsAllowedAndParaffinTissueDistributionAndNotAutomaticTissue()
    {
        PhenotypingStage phenotypingStage = buildPhenotypingStage(
            PhenotypingStageState.PhenotypingRegistered.getInternalName(),
            NOT_AUTOMATIC_TISSUE_CREATION_WORK_UNIT);
        TissueDistribution existingTissueDistribution = new TissueDistribution();
        existingTissueDistribution.setEndDate(LocalDate.now());
        existingTissueDistribution.setMaterialDepositedType(paraffinMaterialDepositedType);
        phenotypingStage.addTissueDistribution(existingTissueDistribution);
        phenotypingStage.setProcessDataEvent(PhenotypingStageEvent.updateToPhenotypingStarted);
        when(policyEnforcement.hasPermission(null, Actions.UPDATE_TO_PHENOTYPING_STARTED)).thenReturn(true);

        testInstance.process(phenotypingStage);

        assertThat(
            "Expected 1 tissue distribution", phenotypingStage.getTissueDistributions().size(), is(1));
        TissueDistribution tissueDistribution = phenotypingStage.getTissueDistributions().iterator().next();
        assertThat(
            "End date should be null", tissueDistribution.getEndDate(), is(existingTissueDistribution.getEndDate()));
        assertThat(
            "Unexpected material deposited type",
            tissueDistribution.getMaterialDepositedType().getName(),
            is(PARAFFIN_EMBEDDED_SECTIONS_NAME));

        verify(
            stageStateSetter, times(1)).setStatusByName(
            any(PhenotypingStage.class),
            eq(PhenotypingStageState.PhenotypingStarted.getInternalName()));
    }

    private PhenotypingStage buildPhenotypingStage(String statusName, String workUnitName)
    {
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        Status status = new Status();
        status.setName(statusName);
        phenotypingStage.setProcessDataStatus(status);
        Plan plan = new Plan();
        PhenotypingAttempt phenotypingAttempt = new PhenotypingAttempt();
        phenotypingAttempt.setPlan(plan);
        phenotypingStage.setPhenotypingAttempt(phenotypingAttempt);
        WorkUnit workUnit = new WorkUnit();
        workUnit.setName(workUnitName);
        plan.setWorkUnit(workUnit);
        return phenotypingStage;
    }
}