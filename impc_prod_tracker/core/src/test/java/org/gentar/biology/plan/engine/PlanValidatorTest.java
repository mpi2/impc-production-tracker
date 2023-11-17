package org.gentar.biology.plan.engine;

import static org.gentar.mockdata.MockData.planMockData;
import static org.gentar.mockdata.MockData.planTypeMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.ColonyValidator;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypeChecker;
import org.gentar.biology.plan.attempt.AttemptTypeService;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptValidator;
import org.gentar.biology.plan.attempt.crispr_allele_modification.CrisprAlleleModificationValidator;
import org.gentar.biology.plan.attempt.es_cell_allele_modification.EsCellAlleleModificationValidator;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypeAttemptValidator;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.engine.ProjectValidator;
import org.gentar.exceptions.CommonErrorMessages;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.parameters.P;


@ExtendWith(MockitoExtension.class)
class PlanValidatorTest {


    @Mock
    private CrisprAttemptValidator crisprAttemptValidator;
    @Mock
    private AttemptTypeService attemptTypeService;
    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;
    @Mock
    private ResourceAccessChecker<Plan> resourceAccessChecker;
    @Mock
    private ProjectValidator projectValidator;
    @Mock
    private PhenotypeAttemptValidator phenotypeAttemptValidator;
    @Mock
    private ColonyValidator colonyValidator;
    @Mock
    private EsCellAlleleModificationValidator esCellAlleleModificationValidator;

    @Mock
    private CrisprAlleleModificationValidator crisprAlleleModificationValidator;

    private static final String ATTEMPT_TYPE_PLAN_TYPE_INVALID_ASSOCIATION =
        "The attempt type [%s] cannot be associated with a plan with type [%s].";
    private static final String NULL_OBJECT_ERROR = "%s cannot be null.";

    PlanValidator testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new PlanValidator(crisprAttemptValidator,
            attemptTypeService,
            policyEnforcement,
            resourceAccessChecker,
            projectValidator,
            phenotypeAttemptValidator,
            colonyValidator,
            esCellAlleleModificationValidator,
            crisprAlleleModificationValidator );
    }

    @Test
    void validatePlanTypeNull() {

        Plan plan = planMockData();
        plan.setPlanType(null);

        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            testInstance.validate(plan);
        });

        String expectedMessage =
            String.format(CommonErrorMessages.NULL_FIELD_ERROR, "typeName");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateAttemptTypeNotNull() {

        Plan plan = planMockData();
        plan.setAttemptType(null);

        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            testInstance.validate(plan);
        });

        String expectedMessage =
            String.format(CommonErrorMessages.NULL_FIELD_ERROR, "attemptTypeName");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateWrongAttemptTypesName() {

        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            testInstance.validate(planMockData());
        });

        String expectedMessage =
            String.format(
                ATTEMPT_TYPE_PLAN_TYPE_INVALID_ASSOCIATION,
                planMockData().getAttemptType().getName(),
                planMockData().getPlanType().getName());
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validate() {

        lenient().when(attemptTypeService.getAttemptTypesByPlanTypeName(
            PlanTypeName.valueOfLabel(planMockData().getPlanType().getName())))
            .thenReturn(List.of(AttemptTypesName.ES_CELL));

        assertDoesNotThrow(() ->
            testInstance.validate(planMockData())
        );
    }

    @Test
    void validateDataCreationAttemptTypeCrispr() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.CRISPR_TYPE);
        plan.setAttemptType(attemptType);

        assertDoesNotThrow(() ->
            testInstance.validateDataCreation(plan)
        );
    }

    @Test
    void validateDataCreationAttemptTypeEsCEll() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.ES_CELL_TYPE);
        plan.setAttemptType(attemptType);

        assertDoesNotThrow(() ->
            testInstance.validateDataCreation(plan)
        );
    }

    @Test
    void validateDataCreationAttemptTypeEsCEllModificationStartingPointsNull() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.ES_CELL_ALLELE_MODIFICATION_TYPE);
        plan.setAttemptType(attemptType);
        plan.setPlanStartingPoints(null);


        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            testInstance.validateDataCreation(plan);
        });

        String expectedMessage =
            String.format(NULL_OBJECT_ERROR, "Starting Point (outcome)");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataCreationAttemptTypeEsCEllModification() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.ES_CELL_ALLELE_MODIFICATION_TYPE);
        plan.setAttemptType(attemptType);


        Mockito.doNothing().when(colonyValidator)
            .validateDataForStartingPoint(Mockito.any(Colony.class));

        assertDoesNotThrow(() ->
            testInstance.validateDataCreation(plan)
        );
    }

    @Test
    void validateDataCreationAttemptTypePhenotypingStartingPointsNull() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.PHENOTYPING_TYPE);
        plan.setAttemptType(attemptType);
        plan.setPlanStartingPoints(null);


        Exception exception = assertThrows(UserOperationFailedException.class, () -> {
            testInstance.validateDataCreation(plan);
        });

        String expectedMessage =
            String.format(NULL_OBJECT_ERROR, "Starting Point (outcome)");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateDataCreationAttemptTypePhenotyping() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.PHENOTYPING_TYPE);
        plan.setAttemptType(attemptType);


        Mockito.doNothing().when(colonyValidator)
            .validateDataForStartingPoint(Mockito.any(Colony.class));

        assertDoesNotThrow(() ->
            testInstance.validateDataCreation(plan)
        );
    }

    @Test
    void validateUpdateAttemptTypeCrispr() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.CRISPR_TYPE);
        plan.setAttemptType(attemptType);

        assertDoesNotThrow(() ->
            testInstance.validateUpdate(plan, plan)
        );

    }

    @Test
    void validateUpdateAttemptTypeEsCEll() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.ES_CELL_TYPE);
        plan.setAttemptType(attemptType);

        assertDoesNotThrow(() ->
            testInstance.validateUpdate(plan, plan)
        );

    }


    @Test
    void validateUpdateAttemptTypeEsCEllModification() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.ES_CELL_ALLELE_MODIFICATION_TYPE);
        plan.setAttemptType(attemptType);

        assertDoesNotThrow(() ->
            testInstance.validateUpdate(plan, plan)
        );

    }


    @Test
    void validateUpdateAttemptTypePhenotyping() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.PHENOTYPING_TYPE);
        plan.setAttemptType(attemptType);

        assertDoesNotThrow(() ->
            testInstance.validateUpdate(plan, plan)
        );

    }

    @Test
    void validateUpdateAttemptTypePhenotypingPhenotypingAttemptNotNull() {

        Plan plan = planMockData();
        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.PHENOTYPING_TYPE);
        plan.setAttemptType(attemptType);
        PhenotypingAttempt phenotypingAttempt = new PhenotypingAttempt();
        plan.setPhenotypingAttempt(phenotypingAttempt);

        assertDoesNotThrow(() ->
            testInstance.validateUpdate(plan, plan)
        );

    }


    @Test
    void validatePermissionToCreatePlanPlanTypeProduction() {
        assertDoesNotThrow(() ->
            testInstance.validatePermissionToCreatePlan(planMockData())
        );
    }

    @Test
    void validatePermissionToCreatePlanPlanTypePhenotyping() {
        Plan plan = planMockData();
        PlanType planType = planTypeMockData();
        planType.setName(PlanTypeName.PHENOTYPING.getLabel());
        plan.setPlanType(planType);
        assertDoesNotThrow(() ->
            testInstance.validatePermissionToCreatePlan(plan)
        );
    }

    @Test
    void validatePermissionToUpdatePlan() {

        Exception exception = assertThrows(ForbiddenAccessException.class, () -> {
            testInstance.validatePermissionToUpdatePlan(planMockData());
        });

        String expectedMessage =
            ("You do not have permission to update the plan PIN:000000001.");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getCheckedCollection() {

        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PLAN_ACTION)))
            .thenReturn(planMockData());

        Collection<Plan> planCollection = new HashSet<>();
        planCollection.add(planMockData());
        List<Plan> plans=testInstance.getCheckedCollection(planCollection);

        assertEquals(plans.size(),1);
    }
    @Test
    void validateReadPermissions() {

        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Plan.class), eq(Actions.READ_PLAN_ACTION)))
            .thenReturn(planMockData());

        assertDoesNotThrow(() ->
            testInstance.validateReadPermissions(planMockData())
        );

    }

    @Test
    void validateReadPermissionsThrow() {

        doThrow(AccessDeniedException.class)
            .when(policyEnforcement)
            .checkPermission(planMockData(), Actions.READ_PLAN_ACTION);


        Exception exception = assertThrows(ForbiddenAccessException.class, () -> {
            testInstance.validateReadPermissions(planMockData());
        });

        String expectedMessage =
            ("You do not have permission to read the plan PIN:000000001.");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));


    }
}