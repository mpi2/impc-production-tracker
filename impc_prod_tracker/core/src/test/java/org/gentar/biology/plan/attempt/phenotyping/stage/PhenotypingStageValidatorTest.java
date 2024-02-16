package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.gentar.mockdata.MockData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
class PhenotypingStageValidatorTest {

    @Mock
    private PhenotypingStageRepository phenotypingStageRepository;
    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;

    PhenotypingStageValidator testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new PhenotypingStageValidator(phenotypingStageRepository, policyEnforcement);
    }

    @Test
    void validateDataPhenotypingAttemptNull() {
        PhenotypingStage phenotypingStage = phenotypingStageMockData();
        phenotypingStage.setPhenotypingAttempt(null);
        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(phenotypingStage));

        String expectedMessage =
            "The phenotyping stage requires a phenotyping attempt.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void validateDataPhenotypingStageTypeNull() {

        PhenotypingStage phenotypingStage = phenotypingStageMockData();
        phenotypingStage.setPhenotypingStageType(null);
        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(phenotypingStage));

        String expectedMessage =
            "The phenotyping stage requires a phenotyping stage type.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void validateData() {


        lenient()
            .when(phenotypingStageRepository.findAllByPhenotypingAttemptAndPhenotypingStageType(
                phenotypingAttemptMockData(), phenotypingStageTypeMockData()))
            .thenReturn(List.of(phenotypingStageMockData()));

        assertDoesNotThrow(() ->
            testInstance.validateData(phenotypingStageMockData())
        );
    }

    @Test
    void validateDataExistingByTypeNull() {

        PhenotypingStage phenotypingStage = phenotypingStageMockData();
        phenotypingStage.setId(2L);
        lenient()
            .when(phenotypingStageRepository.findAllByPhenotypingAttemptAndPhenotypingStageType(
                phenotypingAttemptMockData(), phenotypingStageTypeMockData()))
            .thenReturn(List.of(phenotypingStage));

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(phenotypingStageMockData()));

        String expectedMessage =
            "A phenotyping stage of type [early adult and embryo] already exists for the plan PIN:000000001.";
        String actualMessage = exception.getMessage();


        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void validateCreationPermission() {

        Exception exception = assertThrows(ForbiddenAccessException.class, () -> testInstance.validateCreationPermission(phenotypingStageMockData()));

        String expectedMessage =
            "You do not have permission to create the phenotyping stage. To execute this action you need permission to edit the plan PIN:000000001.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateUpdatePermission() {

        Exception exception = assertThrows(ForbiddenAccessException.class, () -> testInstance.validateUpdatePermission(phenotypingStageMockData()));

        String expectedMessage =
            "You do not have permission to update the phenotyping stage PSN:000000000001. To execute this action you need permission to edit the plan PIN:000000001.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}