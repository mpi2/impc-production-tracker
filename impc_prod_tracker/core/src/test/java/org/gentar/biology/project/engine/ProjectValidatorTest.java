package org.gentar.biology.project.engine;

import static org.gentar.mockdata.MockData.TEST_NAME;
import static org.gentar.mockdata.MockData.attemptTypeMockData;
import static org.gentar.mockdata.MockData.phenotypingStageTypeMockData;
import static org.gentar.mockdata.MockData.planMockData;
import static org.gentar.mockdata.MockData.projectMockData;
import static org.gentar.mockdata.MockData.statusMockData;
import static org.gentar.mockdata.MockData.workUnitMockData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypeChecker;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectQueryHelper;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.work_unit.WorkUnit;
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

@ExtendWith(MockitoExtension.class)
class ProjectValidatorTest {

    private static final String PHENOTYPING_STAGE_STARTED =
        "The project's privacy can not be changed after data " +
            "has been submitted to the DCC.";

    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;
    @Mock
    private ResourceAccessChecker<Project> resourceAccessChecker;
    @Mock
    private ProjectQueryHelper projectQueryHelper;

    ProjectValidator testInstance;

    @BeforeEach
    void setUp() {
        testInstance =
            new ProjectValidator(policyEnforcement, resourceAccessChecker, projectQueryHelper);
    }

    @Test
    void validateData() {

        assertDoesNotThrow(() ->
            testInstance.validateData(projectMockData())
        );

    }

    @Test
    void validateDataPlansNull() {

        Project project = projectMockData();
        project.setPlans(null);

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateData(project));

        String expectedMessage = "There are not plans associated with the project.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void validatePrivacyDataMatchPhenotypingStageFalse() {

        Project oldProject = projectMockData();

        Privacy privacy = new Privacy();

        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.ES_CELL_TYPE);

        PhenotypingStage phenotypingStage = new PhenotypingStage();
        phenotypingStage.setStatus(statusMockData());
        phenotypingStage.setPhenotypingStageType(phenotypingStageTypeMockData());
        PhenotypingAttempt phenotypingAttempt = new PhenotypingAttempt();
        phenotypingAttempt.setPhenotypingStages(Set.of(phenotypingStage));

        Plan plan = new Plan();
        plan.setAttemptType(attemptType);
        plan.setPhenotypingAttempt(phenotypingAttempt);

        Project newProject = projectMockData();

        newProject.setPrivacy(privacy);
        newProject.setPlans(Set.of(plan));

        assertDoesNotThrow(() ->
            testInstance.validatePrivacyData(oldProject, newProject)
        );

    }

    @Test
    void validatePrivacyDataMatchPhenotypingStageTrue() {

        Project oldProject = projectMockData();

        Privacy privacy = new Privacy();

        AttemptType attemptType = new AttemptType();
        attemptType.setName(AttemptTypeChecker.PHENOTYPING_TYPE);

        PhenotypingStage phenotypingStage = new PhenotypingStage();
        phenotypingStage.setPhenotypingStageType(phenotypingStageTypeMockData());
        phenotypingStage.setStatus(statusMockData());

        PhenotypingAttempt phenotypingAttempt = new PhenotypingAttempt();
        phenotypingAttempt.setPhenotypingStages(Set.of(phenotypingStage));
        Plan plan = new Plan();
        plan.setAttemptType(attemptType);
        plan.setPhenotypingAttempt(phenotypingAttempt);

        Project newProject = projectMockData();

        newProject.setPrivacy(privacy);
        newProject.setPlans(Set.of(plan));


        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validatePrivacyData(oldProject, newProject));

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(PHENOTYPING_STAGE_STARTED));
    }

    @Test
    void validatePermissionToCreateProjectHasPermission() {

        lenient().when(policyEnforcement.hasPermission(any(), anyString())).thenReturn(true);

        assertDoesNotThrow(() ->
            testInstance.validatePermissionToCreateProject(projectMockData())
        );

    }

    @Test
    void validatePermissionToCreateProject() {


        Exception exception = assertThrows(ForbiddenAccessException.class, () -> testInstance.validatePermissionToCreateProject(projectMockData()));

        String expectedMessage = "You do not have permission to create the project.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


    @Test
    void validatePermissionToUpdateProjectHasPermission() {

        lenient().when(policyEnforcement.hasPermission(any(), anyString())).thenReturn(true);

        assertDoesNotThrow(() ->
            testInstance.validatePermissionToUpdateProject(projectMockData())
        );
    }


    @Test
    void validatePermissionToUpdateProject() {

        Exception exception = assertThrows(ForbiddenAccessException.class, () -> testInstance.validatePermissionToUpdateProject(projectMockData()));

        String expectedMessage = "You do not have permission to update the project TPN:000000001.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validatePermissionToAddProductionPlan() {
        Exception exception = assertThrows(ForbiddenAccessException.class, () -> testInstance.validatePermissionToAddProductionPlan(projectMockData()));

        String expectedMessage =
            "You do not have permission to create the plan. You do not have permission to update the project TPN:000000001.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validatePermissionToAddProductionPlanHasPermittion() {
        lenient().when(policyEnforcement.hasPermission(any(), anyString())).thenReturn(true);

        assertDoesNotThrow(() ->
            testInstance.validatePermissionToAddProductionPlan(projectMockData())
        );
    }

    @Test
    void validatePermissionToAddPhenotypingPlanNoPermition() {
        Exception exception = assertThrows(ForbiddenAccessException.class, () -> testInstance.validatePermissionToAddPhenotypingPlan(projectMockData()));

        String expectedMessage =
            "You do not have permission to create the plan. You do not have permission to edit the project [tpn=TPN:000000001,assignmentStatus=null].";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void validatePermissionToAddPhenotypingPlanWithReadProjectActionPermition() {

        lenient().when(policyEnforcement.hasPermission(any(), anyString())).thenReturn(true);
        Exception exception = assertThrows(ForbiddenAccessException.class, () -> testInstance.validatePermissionToAddPhenotypingPlan(projectMockData()));

        String expectedMessage =
            "You do not have permission to create the plan. At least one production plan is required.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void validatePermissionToAddPhenotypingPlanWithAllPermition() {

        lenient().when(policyEnforcement.hasPermission(any(), anyString())).thenReturn(true);
        lenient().when(projectQueryHelper.getPlansByType(any(), any()))
            .thenReturn(List.of(planMockData()));

        assertDoesNotThrow(() ->
            testInstance.validatePermissionToAddPhenotypingPlan(projectMockData())
        );
    }


    @Test
    void validateReadPermissions() {

        doThrow(AccessDeniedException.class)
            .when(policyEnforcement)
            .checkPermission(Mockito.any(Project.class), eq(Actions.READ_PROJECT_ACTION));


        Exception exception = assertThrows(ForbiddenAccessException.class, () -> testInstance.validateReadPermissions(projectMockData()));

        String expectedMessage =
            ("You do not have permission to read the project TPN:000000001.");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateReadPermissionsHasPermission() {

        assertDoesNotThrow(() ->
            testInstance.validateReadPermissions(projectMockData())
        );

    }

    @Test
    void getAccessChecked() {

        assertDoesNotThrow(() ->
            testInstance.getAccessChecked(projectMockData())
        );
    }

    @Test
    void getAccessCheckedPrivacy() {
        lenient().when(resourceAccessChecker.getUserAccessLevel(List.of(TEST_NAME)))
            .thenReturn(List.of(TEST_NAME));

        List<String> names = testInstance.getAccessChecked(List.of(TEST_NAME));

        assertEquals(names, List.of(TEST_NAME));
    }

    @Test
    void getCheckedCollection() {

        Collection<Project> projectCollection = new HashSet<>();
        projectCollection.add(projectMockData());

        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Project.class), eq(Actions.READ_PROJECT_ACTION)))
            .thenReturn(projectMockData());

        List<Project> projects = testInstance.getCheckedCollection(projectCollection);

        assertEquals(projects.getFirst().getTpn(), projectMockData().getTpn());
    }

    @Test
    void validateProductionAttemptTypeCrspr() {
        assertDoesNotThrow(() ->
            testInstance.validateProductionAttempt(projectMockData(), planMockData())
        );
    }

    @Test
    void validateProductionAttempt() {
        assertDoesNotThrow(() ->
            testInstance.validateProductionAttempt(projectMockData(), planMockData())
        );
    }

    @Test
    void validateProductionAttemptTypeEsCEllModification() {

        Project project = projectMockData();

        AttemptType firstAttemptType = attemptTypeMockData();
        firstAttemptType.setName(AttemptTypeChecker.CRISPR_TYPE);

        AttemptType attemptType = attemptTypeMockData();
        attemptType.setName(AttemptTypeChecker.ES_CELL_ALLELE_MODIFICATION_TYPE);

        Plan firstPlan = planMockData();
        firstPlan.setAttemptType(firstAttemptType);

        project.setPlans(Set.of(firstPlan));

        Plan plan = planMockData();
        plan.setAttemptType(attemptType);

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateProductionAttempt(project, plan));

        String expectedMessage =
            ("[es cell allele modification] attempts are not allowed in this project.");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateProductionAttemptTypeHaploEssential() {

        Project project = projectMockData();

        AttemptType firstAttemptType = attemptTypeMockData();
        firstAttemptType.setName(AttemptTypeChecker.ES_CELL_TYPE);

        AttemptType attemptType = attemptTypeMockData();
        attemptType.setName(AttemptTypeChecker.HAPLO_ESSENTIAL_CRISPR_TYPE);

        Plan firstPlan = planMockData();
        firstPlan.setAttemptType(firstAttemptType);

        project.setPlans(Set.of(firstPlan));

        Plan plan = planMockData();
        plan.setAttemptType(attemptType);

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateProductionAttempt(project, plan));

        String expectedMessage =
            ("[haplo-essential crispr] attempts are not allowed in this project.");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateProductionAttemptWorkUnitsEqual() {

        Project project = projectMockData();

        AttemptType firstAttemptType = attemptTypeMockData();
        firstAttemptType.setName(AttemptTypeChecker.ES_CELL_TYPE);

        AttemptType attemptType = attemptTypeMockData();
        attemptType.setName(AttemptTypeChecker.PHENOTYPING_TYPE);

        Plan firstPlan = planMockData();
        firstPlan.setAttemptType(firstAttemptType);
        firstPlan.setWorkUnit(workUnitMockData());

        project.setPlans(Set.of(firstPlan));

        WorkUnit workunit= workUnitMockData();
        workunit.setName("work unit name");

        Plan plan = planMockData();
        plan.setAttemptType(attemptType);
        plan.setWorkUnit(workUnitMockData());
        plan.setWorkUnit(workunit);

        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.validateProductionAttempt(project, plan));

        String expectedMessage =
            ("Your work unit [work unit name] cannot create production plans in this project.");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}