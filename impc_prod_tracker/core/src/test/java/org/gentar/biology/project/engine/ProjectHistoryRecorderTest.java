package org.gentar.biology.project.engine;

import static org.gentar.mockdata.MockData.historyMockData;
import static org.gentar.mockdata.MockData.projectMockData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import org.gentar.audit.history.HistoryService;
import org.gentar.biology.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectHistoryRecorderTest {

    @Mock
    private HistoryService<Project> historyService;

    ProjectHistoryRecorder testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new ProjectHistoryRecorder(historyService);
    }


    @Test
    void saveProjectChangesInHistory() {

        lenient().when( historyService.detectTrackOfChanges(
            any(), any(), any())).thenReturn(historyMockData());
        assertDoesNotThrow(() ->
            testInstance.saveProjectChangesInHistory(projectMockData(), projectMockData())
        );
    }

    @Test
    void saveProjectChangesInHistoryNull() {

        assertDoesNotThrow(() ->
            testInstance.saveProjectChangesInHistory(projectMockData(), projectMockData())
        );
    }
}