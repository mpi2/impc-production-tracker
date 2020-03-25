package org.gentar.biology.project.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class AssignmentStatusUpdaterTest
{
    @Mock
    private AssignmentStatusService assignmentStatusService;

    @Mock
    private ConflictsChecker conflictsChecker;

    private AssignmentStatusUpdater testInstance;

    @BeforeEach
    public void setup()
    {
        testInstance = new AssignmentStatusUpdater(conflictsChecker, assignmentStatusService);
    }
}
