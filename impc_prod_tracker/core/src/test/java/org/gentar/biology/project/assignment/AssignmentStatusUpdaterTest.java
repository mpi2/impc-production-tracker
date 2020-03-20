package org.gentar.biology.project.assignment;

import org.gentar.biology.project.Project;
import org.gentar.test_util.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignmentStatusUpdaterTest
{
    @Mock
    private AssignmentStatusService assignmentStatusService;
    @Mock
    private ConflictsChecker conflictsChecker;

    private AssignmentStatusUpdater testInstance;

    private static AssignmentStatus withdrawn =
        new AssignmentStatus(1L, AssignmentStatusNames.WITHDRAWN_STATUS_NAME, "");

    @BeforeEach
    public void setup()
    {
        testInstance = new AssignmentStatusUpdater(assignmentStatusService, conflictsChecker);
    }

    @Test
    public void testShouldBeWithdraw()
    {
        Project project = ProjectBuilder.getInstance().withWithdrawn(true).build();
        when(
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.WITHDRAWN_STATUS_NAME))
            .thenReturn(withdrawn);
        AssignmentStatus assignmentStatus = testInstance.calculateAssignmentStatus(project);
        assertThat("Incorrect Assignment status:", assignmentStatus.getName(), is("Withdrawn"));
    }

    @Test
    public void testShouldNotBeWithdraw()
    {
        Project project = ProjectBuilder.getInstance()
            .withWithdrawn(true)
            .withProjectAssignmentStatus(AssignmentStatusNames.WITHDRAWN_STATUS_NAME)
            .build();
        AssignmentStatus assignmentStatus = testInstance.calculateAssignmentStatus(project);
        assertThat("Incorrect Assignment status:", assignmentStatus, is(nullValue()));
    }
}