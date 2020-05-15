package org.gentar.biology.project.summary_status;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.assignment.AssignmentStatusChecker;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusNames;
import org.gentar.biology.status.StatusService;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class has the logic to update the summary status in a project.
 */
@Component
public class ProjectSummaryStatusUpdater
{
    private StatusService statusService;

    public ProjectSummaryStatusUpdater(StatusService statusService)
    {
        this.statusService = statusService;
    }

    /**
     * Updates the project with the corresponding summary status, which is based in the summary
     * status in their plans.
     * @param project Project to be updated.
     */
    public void calculateSummaryStatus(Project project)
    {
        Status summaryStatus;
        if (AssignmentStatusChecker.projectIsInactive(project))
        {
            summaryStatus = statusService.getStatusByName(StatusNames.INACTIVE);
        }
        else
        {
            summaryStatus = getMostAdvancedSummaryStatusFromPlans(project);
        }
        if (summaryStatus != null)
        {
            setAndLogNewSummaryStatus(project, summaryStatus);
        }
    }

    private Status getMostAdvancedSummaryStatusFromPlans(Project project)
    {
        Status mostAdvancedSummaryStatus = null;
        Set<Plan> plans = project.getPlans();
        if (plans != null)
        {
            List<Status> summaryStatusesNotAbortedPlans =
                plans.stream()
                    .filter(x -> !x.getStatus().getIsAbortionStatus())
                    .map(Plan::getSummaryStatus)
                    .collect(Collectors.toList());
            mostAdvancedSummaryStatus = summaryStatusesNotAbortedPlans
                .stream()
                .max(Comparator.comparing(Status::getOrdering))
                .orElse(null);
        }
        return mostAdvancedSummaryStatus;
    }

    private void setAndLogNewSummaryStatus(Project project, Status summaryStatus)
    {
        Project originalProject = new Project(project);
        Status currentSummaryStatus = originalProject.getSummaryStatus();
        String currentSummaryStatusName =
            currentSummaryStatus == null ? "" : currentSummaryStatus.getName();
        if (!currentSummaryStatusName.equals(summaryStatus.getName()))
        {
            project.setSummaryStatus(summaryStatus);
            registerSummaryStatusStamp(project);
        }
    }

    private void registerSummaryStatusStamp(Project project)
    {
        Set<ProjectSummaryStatusStamp> stamps = project.getSummaryStatusStamps();
        if (stamps == null)
        {
            stamps = new HashSet<>();
        }
        ProjectSummaryStatusStamp projectSummaryStatusStamp = new ProjectSummaryStatusStamp();
        projectSummaryStatusStamp.setProject(project);
        projectSummaryStatusStamp.setStatus(project.getSummaryStatus());
        projectSummaryStatusStamp.setDate(LocalDateTime.now());
        stamps.add(projectSummaryStatusStamp);
        project.setSummaryStatusStamps(stamps);
    }
}
