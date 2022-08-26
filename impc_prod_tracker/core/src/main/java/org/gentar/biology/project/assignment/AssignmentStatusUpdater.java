package org.gentar.biology.project.assignment;

import org.gentar.biology.project.Project;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class in charge of setting and updating the Assignment Status for a project depending on its
 * conditions.
 */
@Component
public class AssignmentStatusUpdater
{
    private ConflictsChecker conflictsChecker;
    private AssignmentStatusService assignmentStatusService;

    public AssignmentStatusUpdater(
        ConflictsChecker conflictsChecker, AssignmentStatusService assignmentStatusService)
    {
        this.conflictsChecker = conflictsChecker;
        this.assignmentStatusService = assignmentStatusService;
    }

    /**
     * Method to recalculate assignment statuses when the conflict is susceptible to change because
     * another conflicting project has changed. If the project is Inactive or Assigned, no changes
     * are applied because they are statuses that depend on the plans in the project and not in
     * other projects.
     * @param project Project being evaluated
     */
    public void recalculateConflicts(Project project)
    {
        boolean projectIsInactive = AssignmentStatusChecker.projectHasStatusByName(
            project, AssignmentStatusNames.INACTIVE_STATUS_NAME);
        boolean projectIsAssigned =
            AssignmentStatusChecker.projectHasStatusByName(
                project, AssignmentStatusNames.ASSIGNED_STATUS_NAME);
        if (!(projectIsInactive || projectIsAssigned))
        {
            setCalculatedAssignmentStatus(project);
        }
    }

    private void setCalculatedAssignmentStatus(Project project)
    {
        AssignmentStatus status = conflictsChecker.checkConflict(project);
        Project original = new Project(project);
        setAndLogNewAssignmentStatus(original, project, status);
    }

    private void setAndLogNewAssignmentStatus(
        Project originalProject, Project newProject, AssignmentStatus assignmentStatus)
    {
        String currentAssignmentStatusName =
            originalProject.getAssignmentStatus() == null ? "" :
                originalProject.getAssignmentStatus().getName();
        if (!currentAssignmentStatusName.equals(assignmentStatus.getName()))
        {
            newProject.setAssignmentStatus(assignmentStatus);
            registerAssignmentStatusStamp(newProject);
        }
    }

    /**
     * A project needs to be inactivated if all its plans are aborted.
     * @param project Project to evaluate if needs to be inactivated.
     */
    public void inactivateOrActivateProjectIfNeeded(Project project)
    {
        var plans = project.getPlans();
        boolean areAllPlansAborted = false;
        if (plans != null)
        {
            areAllPlansAborted = plans.stream().allMatch(x -> x.getProcessDataStatus().getIsAbortionStatus());
        }
        if (areAllPlansAborted)
        {
            inactivateProject(project);
        }
        else if (AssignmentStatusChecker.projectIsInactive(project))
        {
            reactivateProject(project);
        }
    }

    /**
     * Inactivates a project, meaning the Assignment Status is inactive.
     * @param project The project to be inactivated.
     */
    private void inactivateProject(Project project)
    {
        Project original = new Project(project);
        AssignmentStatus inactive =
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.INACTIVE_STATUS_NAME);
        setAndLogNewAssignmentStatus(original, project, inactive);
    }

    private void reactivateProject(Project project)
    {
        setCalculatedAssignmentStatus(project);
    }

    private void registerAssignmentStatusStamp(Project project)
    {
        Set<AssignmentStatusStamp> stamps = project.getAssignmentStatusStamps();
        if (stamps == null)
        {
            stamps = new HashSet<>();
        }
        AssignmentStatusStamp assignmentStatusStamp = new AssignmentStatusStamp();
        assignmentStatusStamp.setProject(project);
        assignmentStatusStamp.setAssignmentStatus(project.getAssignmentStatus());
        assignmentStatusStamp.setDate(LocalDateTime.now());
        stamps.add(assignmentStatusStamp);
        project.setAssignmentStatusStamps(stamps);
    }

    /**
     * Given that a project was updated (its status of information in entities under it), then check
     * other projects that can br conflicting with it, searching for a potential change in that
     * conflict status (a new Assignment Status)
     * @param project Project to take as reference
     */
    public void updateConflictingProjects(Project project)
    {
        List<Project> conflictingProjects = conflictsChecker.getProjectsSameDeletionIntention(project);
        conflictingProjects.forEach(this::recalculateConflicts);
    }
}
