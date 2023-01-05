package org.gentar.biology.project.engine;

import static org.gentar.mockdata.MockData.projectMockData;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.engine.PlanCreator;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.assignment.AssignmentStatusUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
class ProjectCreatorTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private AssignmentStatusUpdater assignmentStatusUpdater;
    @Mock
    private HistoryService<Project> historyService;
    @Mock
    private ProjectValidator projectValidator;
    @Mock
    private PlanCreator planCreator;

    ProjectCreator testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new ProjectCreator(entityManager, assignmentStatusUpdater,
            historyService,
            projectValidator,
            planCreator);


    }

    @Test
    void createProject() {
        Project project = testInstance.createProject(projectMockData());

        assertEquals(project.getTpn(),projectMockData().getTpn());
    }
}