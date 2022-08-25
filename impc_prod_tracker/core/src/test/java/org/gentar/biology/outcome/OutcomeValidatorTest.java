package org.gentar.biology.outcome;

import static org.gentar.mockdata.MockData.TPN_000000001;
import static org.gentar.mockdata.MockData.outcomeMockData;
import static org.gentar.mockdata.MockData.planMockData;
import static org.gentar.mockdata.MockData.projectMockData;
import static org.junit.jupiter.api.Assertions.*;

import org.gentar.biology.colony.ColonyValidator;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutcomeValidatorTest {

    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;
    @Mock
    private ColonyValidator colonyValidator;

    OutcomeValidator testInstance;


    @BeforeEach
    void setUp() {
        testInstance =new OutcomeValidator(policyEnforcement,colonyValidator);
    }

    @Test
    void validateDataOutcomeTypeColony() {
        Outcome outcome = new Outcome();
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setName("Colony");
        outcome.setOutcomeType(outcomeType);
        assertDoesNotThrow(() ->
            testInstance.validateData(outcome)
        );
    }

    @Test
    void validateDataOutcomeTypeSpecimen() {
        Outcome outcome = new Outcome();
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setName("Specimen");
        outcome.setOutcomeType(outcomeType);
        assertDoesNotThrow(() ->
            testInstance.validateData(outcome)
        );
    }

    @Test
    void validateDataOutcomeNull() {
        Outcome outcome = new Outcome();
        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            testInstance.validateData(outcome);
        });

        String expectedMessage = "cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateUpdateData() {
        Outcome outcome = new Outcome();
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setName("Colony");
        outcome.setOutcomeType(outcomeType);
        assertDoesNotThrow(() ->
            testInstance.validateUpdateData(outcome,outcome)
        );
    }

    @Test
    void validateCreationPermission() {
        Outcome outcome = new Outcome();
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setName("Colony");
        outcome.setOutcomeType(outcomeType);
        Plan plan =new Plan();
        plan.setId(1l);
        outcome.setPlan(plan);

        Exception exception = assertThrows(ForbiddenAccessException.class, () -> {
            testInstance.validateCreationPermission(outcome);
        });

        String expectedMessage = "You do not have permission to create the outcome. To execute this action you need permission to edit the plan null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateUpdatePermission() {

        Outcome outcome = new Outcome();
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setName("Colony");
        outcome.setOutcomeType(outcomeType);
        Plan plan =new Plan();
        plan.setId(1l);
        outcome.setPlan(plan);

        Exception exception = assertThrows(ForbiddenAccessException.class, () -> {
            testInstance.validateUpdatePermission(outcome);
        });

        String expectedMessage = "You do not have permission to update the outcome null. To execute this action you need permission to edit the plan null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateReadPermissions() {
        Outcome outcome = new Outcome();
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setName("Colony");
        outcome.setOutcomeType(outcomeType);
        Plan plan =new Plan();
        plan.setId(1l);
        outcome.setPlan(plan);

        Exception exception = assertThrows(ForbiddenAccessException.class, () -> {
            testInstance.validateReadPermissions(outcome);
        });

        String expectedMessage = "You do not have permission to read the outcome null. The outcome is linked to the plan null and you do not have permission to read it.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}