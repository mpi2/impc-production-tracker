package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeName;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PhenotypingStageStateSetterTest
{
    private PhenotypingStageStateSetter testInstance;

    @Mock
    private StatusService statusService;

    @BeforeEach
    void setUp()
    {
        testInstance = new PhenotypingStageStateSetter(statusService);
    }

    @Test
    void setStatusByNameToEarlyAdult()
    {
        String statusNameGivenByStateMachine = "Status Name";
        String expectedStatusName = "Status Name";
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        PhenotypingStageType phenotypingStageType = new PhenotypingStageType();
        phenotypingStageType.setName(PhenotypingStageTypeName.EARLY_ADULT.getLabel());
        phenotypingStage.setPhenotypingStageType(phenotypingStageType);
        Status obtainedStatus = new Status();
        obtainedStatus.setName(expectedStatusName);

        testInstance.setStatusByName(phenotypingStage, statusNameGivenByStateMachine);

        verify(statusService).getStatusByName(expectedStatusName);
    }

    @Test
    void setStatusByNameToLateAdult()
    {
        String statusNameGivenByStateMachine = "Status Name";
        String expectedStatusName = "Late Adult Status Name";
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        PhenotypingStageType phenotypingStageType = new PhenotypingStageType();
        phenotypingStageType.setName(PhenotypingStageTypeName.LATE_ADULT.getLabel());
        phenotypingStage.setPhenotypingStageType(phenotypingStageType);
        Status obtainedStatus = new Status();
        obtainedStatus.setName(expectedStatusName);

        testInstance.setStatusByName(phenotypingStage, statusNameGivenByStateMachine);

        verify(statusService).getStatusByName(expectedStatusName);
    }

    @Test
    void setStatusByNameToHaploessential()
    {
        String statusNameGivenByStateMachine = "Status Name";
        String expectedStatusName = "Haplo-Essential Status Name";
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        PhenotypingStageType phenotypingStageType = new PhenotypingStageType();
        phenotypingStageType.setName(PhenotypingStageTypeName.HAPLOESSENTIAL.getLabel());
        phenotypingStage.setPhenotypingStageType(phenotypingStageType);
        Status obtainedStatus = new Status();
        obtainedStatus.setName(expectedStatusName);

        testInstance.setStatusByName(phenotypingStage, statusNameGivenByStateMachine);

        verify(statusService).getStatusByName(expectedStatusName);
    }

    @Test
    void setStatusByNameToEmbryo()
    {
        String statusNameGivenByStateMachine = "Status Name";
        String expectedStatusName = "Embryo Status Name";
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        PhenotypingStageType phenotypingStageType = new PhenotypingStageType();
        phenotypingStageType.setName(PhenotypingStageTypeName.EMBRYO.getLabel());
        phenotypingStage.setPhenotypingStageType(phenotypingStageType);
        Status obtainedStatus = new Status();
        obtainedStatus.setName(expectedStatusName);

        testInstance.setStatusByName(phenotypingStage, statusNameGivenByStateMachine);

        verify(statusService).getStatusByName(expectedStatusName);
    }
}