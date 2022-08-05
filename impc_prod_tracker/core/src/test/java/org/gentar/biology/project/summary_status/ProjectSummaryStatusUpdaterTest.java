package org.gentar.biology.project.summary_status;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.assignment.AssignmentStatus;
import org.gentar.biology.project.assignment.AssignmentStatusNames;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusNames;
import org.gentar.biology.status.StatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectSummaryStatusUpdaterTest
{
    private ProjectSummaryStatusUpdater testInstance;

    @Mock
    private StatusService statusService;

    private static final Status INACTIVE = new Status(1L, "Inactive", "", 0, false);

    private static final Status STATUS_1 = new Status(1L, "Status1", "Status1", 1, false);
    private static final Status STATUS_2 = new Status(2L, "Status2", "Status2", 2, false);
    private static final Status STATUS_ABORTED =
        new Status(5L, "Attempt Aborted", "Attempt Aborted", 100, true);

    @BeforeEach
    public void setup()
    {
        testInstance = new ProjectSummaryStatusUpdater(statusService);
    }

    @Test
    void testCalculateSummaryStatusWhenProjectInactive()
    {
        Project project =
            buildProjectWithAssignmentStatus(AssignmentStatusNames.INACTIVE_STATUS_NAME);
        when(statusService.getStatusByName(StatusNames.INACTIVE)).thenReturn(INACTIVE);

        testInstance.calculateSummaryStatus(project);

        assertThat("Summary Status cannot be null", project.getSummaryStatus(), is(notNullValue()));
        assertThat(
            "Incorrect Summary status:", project.getSummaryStatus().getName(), is("Inactive"));
    }

    @Test
    void testCalculateSummaryStatusWhenOnePlanAndActive()
    {
        Plan plan = new Plan();
        plan.setPlanStatus(STATUS_1);
        plan.setSummaryStatus(STATUS_1);
        Project project =
            buildProjectWithAssignmentStatus(AssignmentStatusNames.ASSIGNED_STATUS_NAME);
        Set<Plan> plans = new HashSet<>(Collections.singletonList(plan));
        project.setPlans(plans);

        testInstance.calculateSummaryStatus(project);

        assertThat("Summary Status cannot be null", project.getSummaryStatus(), is(notNullValue()));
        assertThat(
            "Incorrect Summary status:",
            project.getSummaryStatus().getName(),
            is(STATUS_1.getName()));
    }

    @Test
    void testCalculateSummaryStatusWhenTwoPlansAndActive()
    {
        Plan plan1 = buildPlanWithStatus(1L, STATUS_1);
        Plan plan2 = buildPlanWithStatus(2L, STATUS_2);
        Project project =
            buildProjectWithAssignmentStatus(AssignmentStatusNames.ASSIGNED_STATUS_NAME);
        Set<Plan> plans = new HashSet<>(Arrays.asList(plan1, plan2));
        project.setPlans(plans);

        testInstance.calculateSummaryStatus(project);

        assertThat("Summary Status cannot be null", project.getSummaryStatus(), is(notNullValue()));
        assertThat(
            "Incorrect Summary status:",
            project.getSummaryStatus().getName(),
            is(STATUS_2.getName()));
    }

    @Test
    void testCalculateSummaryStatusWhenActivePlanAndAbortedPlan()
    {
        Plan plan1 = buildPlanWithStatus(1L, STATUS_1);
        Plan plan2 = buildPlanWithStatus(2L, STATUS_ABORTED);
        Project project =
            buildProjectWithAssignmentStatus(AssignmentStatusNames.ASSIGNED_STATUS_NAME);
        Set<Plan> plans = new HashSet<>(Arrays.asList(plan1, plan2));
        project.setPlans(plans);

        testInstance.calculateSummaryStatus(project);

        assertThat("Summary Status cannot be null", project.getSummaryStatus(), is(notNullValue()));
        assertThat(
            "Incorrect Summary status:",
            project.getSummaryStatus().getName(),
            is(STATUS_1.getName()));
    }

    private Project buildProjectWithAssignmentStatus(String assignmentStatusName)
    {
        Project project = new Project();
        AssignmentStatus assignmentStatus = new AssignmentStatus();
        assignmentStatus.setName(assignmentStatusName);
        project.setAssignmentStatus(assignmentStatus);
        return project;
    }

    private Plan buildPlanWithStatus(Long id, Status status)
    {
        Plan plan = new Plan();
        plan.setId(id);
        plan.setPin("PIN:"+id);
        plan.setPlanStatus(status);
        plan.setSummaryStatus(status);
        return plan;
    }
}
