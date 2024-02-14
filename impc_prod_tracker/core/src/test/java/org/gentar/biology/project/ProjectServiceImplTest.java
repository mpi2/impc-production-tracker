package org.gentar.biology.project;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.ortholog.OrthologService;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanValidator;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.engine.ProjectCreator;
import org.gentar.biology.project.engine.ProjectUpdater;
import org.gentar.biology.project.engine.ProjectValidator;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

import static org.gentar.mockdata.MockData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private HistoryService<Project> historyService;
    @Mock
    private ProjectCreator projectCreator;
    @Mock
    private OrthologService orthologService;
    @Mock
    private ProjectQueryHelper projectQueryHelper;
    @Mock
    private ProjectUpdater projectUpdater;
    @InjectMocks
    private ProjectValidator projectValidator;
    @Mock
    private PlanValidator planValidator;
    @Mock
    private ContextAwarePolicyEnforcement policyEnforcement;
    @Mock
    private ResourceAccessChecker<Project> resourceAccessChecker;


    private ProjectServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new ProjectServiceImpl(projectRepository,
            historyService,
            projectCreator,
            orthologService,
            projectQueryHelper,
            projectUpdater,
            projectValidator,
            planValidator);

        projectValidator =
            new ProjectValidator(policyEnforcement, resourceAccessChecker, projectQueryHelper);
    }


    @Test
    void getNotNullProjectByTpn() {
        lenient().when(projectRepository.findByTpn(TPN_000000001)).thenReturn(projectMockData());
        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Project.class), eq(Actions.READ_PROJECT_ACTION)))
            .thenReturn(projectMockData());
//        lenient().when(projectQueryHelper.getIntentionGenesByProject(Mockito.any(Project.class))).thenReturn(projectIntentionGeneListMockData());
        Project project = testInstance.getNotNullProjectByTpn(TPN_000000001);
        assertEquals(project.getTpn(), TPN_000000001);
    }

    @Test
    void getNotNullProjectByTpnIfProjectNull() {
        lenient().when(projectRepository.findByTpn(TPN_000000001)).thenReturn(null);
        Exception exception = assertThrows(NotFoundException.class, () -> {
            Project project = testInstance.getNotNullProjectByTpn(TPN_000000001);
        });

        String expectedMessage = "Project " + TPN_000000001 + " does not exist.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void getProjectByPinWithoutCheckPermissions() {
        lenient().when(projectRepository.findByTpn(TPN_000000001)).thenReturn(projectMockData());
        Project project = testInstance.getProjectByPinWithoutCheckPermissions(TPN_000000001);
        assertEquals(project.getTpn(), TPN_000000001);
    }

    @Test
    void getProjects() {

        ProjectFilter projectFilter =
            ProjectFilterBuilder.getInstance().withTpns(List.of(TPN_000000001)).build();


        lenient().when(projectRepository.findAll(Mockito.any(Specification.class)))
            .thenReturn(Collections.singletonList(projectMockData()));

        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Project.class), eq(Actions.READ_PROJECT_ACTION)))
            .thenReturn(projectMockData());

        List<Project> project = testInstance.getProjects(projectFilter);
        assertEquals(project.getFirst().getTpn(), TPN_000000001);
    }

    @Test
    void getProjectsWithoutOrthologs() {
        ProjectFilter projectFilter =
            ProjectFilterBuilder.getInstance().withTpns(List.of(TPN_000000001)).build();


        lenient().when(projectRepository.findAll(Mockito.any(Specification.class)))
            .thenReturn(Collections.singletonList(projectMockData()));

        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Project.class), eq(Actions.READ_PROJECT_ACTION)))
            .thenReturn(projectMockData());

        List<Project> project = testInstance.getProjectsWithoutOrthologs(projectFilter);
        assertEquals(project.getFirst().getTpn(), TPN_000000001);
    }

    @Test
    void GetProjectsPagable() {
        ProjectFilter projectFilter =
            ProjectFilterBuilder.getInstance().withTpns(List.of(TPN_000000001)).build();

        Pageable pageable = PageRequest.of(0, 1);
        final Page<Project> page = new PageImpl<>(Collections.singletonList(projectMockData()));
        lenient().when(projectRepository.findAll(Mockito.any(Specification.class), eq(pageable)))
            .thenReturn(page);

        lenient().when(resourceAccessChecker
            .checkAccess(Mockito.any(Project.class), eq(Actions.READ_PROJECT_ACTION)))
            .thenReturn(projectMockData());

        Page<Project> projects = testInstance.getProjects(projectFilter, pageable);
        assertEquals(projects.getTotalElements(), 1);
    }

    @Test
    void createProject() {
        lenient().when(projectCreator.createProject(Mockito.any(Project.class)))
            .thenReturn(projectMockData());
        Project project = testInstance.createProject(projectMockData());
        assertEquals(project.getTpn(), TPN_000000001);
    }

    @Test
    void updateProject() {
        History exception = assertDoesNotThrow(() ->
            testInstance.updateProject(projectMockData(), projectMockData())
        );

        assertNull(exception);
    }

    @Test
    void getProjectHistory() {
        lenient().when(historyService.getHistoryByEntityNameAndEntityId("Project", 1L))
            .thenReturn(List.of(historyMockData()));
        List<History> history = testInstance.getProjectHistory(projectMockData());
        assertEquals(history.getFirst().getComment(), TEST_COMMENT);
    }

    @Test
    void checkForUpdates() {
        assertDoesNotThrow(() ->
            testInstance.checkForUpdates(projectMockData())
        );
    }

    @Test
    void associatePlanToProject() {
        Mockito.doNothing().when(planValidator).validate(planMockData());

        assertDoesNotThrow(() ->
            testInstance.associatePlanToProject(planMockData(), projectMockData())
        );
    }

    @Test
    void associatePlanToProjectProjectNull() {
        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.associatePlanToProject(planMockData(), null));
        String expectedMessage =  "The plan cannot be associated with the project because the project is null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void associatePlanToProjectWhenPlanNull() {
        Exception exception = assertThrows(UserOperationFailedException.class, () -> testInstance.associatePlanToProject(null, projectMockData()));
        String expectedMessage = "The plan cannot be associated with the project because the plan is null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void getProductionOutcomesByProject() {
        lenient().when(projectQueryHelper
            .getPlansByType(Mockito.any(Project.class), eq(PlanTypeName.PRODUCTION)))
            .thenReturn(List.of(planMockData()));
        List<Outcome> outcomes = testInstance.getProductionOutcomesByProject(projectMockData());
        assertEquals(outcomes.getFirst().getId(), 1L);
    }

    @Test
    void getFirstProductionPlan() {

        Plan plan = testInstance.getFirstProductionPlan(projectMockData());
        assertEquals(plan.getId(), 1L);
    }

}