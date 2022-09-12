package org.gentar.biology.project.engine;

import static org.gentar.mockdata.MockData.projectMockData;
import static org.junit.jupiter.api.Assertions.*;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectRepository;
import org.gentar.biology.project.assignment.AssignmentStatusUpdater;
import org.gentar.biology.project.summary_status.ProjectSummaryStatusUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ProjectUpdaterTest {

    @Mock
    private HistoryService<Project> historyService;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private AssignmentStatusUpdater assignmentStatusUpdater;
    @Mock
    private ProjectSummaryStatusUpdater projectSummaryStatusUpdater;
    @Mock
    private ProjectHistoryRecorder projectHistoryRecorder;
    @Mock
    private ProjectValidator projectValidator;

    ProjectUpdater testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new ProjectUpdater(historyService,
            projectRepository,
            assignmentStatusUpdater,
            projectSummaryStatusUpdater,
            projectHistoryRecorder,
            projectValidator);
    }

    @Test
    void updateProject() {
        History exception = assertDoesNotThrow(() ->
            testInstance.updateProject(projectMockData(), projectMockData())
        );

        assertNull(exception);
    }

    @Test
    void updateProjectWhenPlanUpdated() {

         assertDoesNotThrow(() ->
            testInstance.updateProjectWhenPlanUpdated(projectMockData())
        );
    }
}