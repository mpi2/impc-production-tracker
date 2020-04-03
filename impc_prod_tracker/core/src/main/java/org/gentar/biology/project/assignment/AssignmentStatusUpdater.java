package org.gentar.biology.project.assignment;

import org.gentar.audit.history.History;
import org.gentar.audit.history.HistoryService;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.state.BreedingPlanState;
import org.gentar.biology.plan.engine.state.LateAdultPhenotypePlanState;
import org.gentar.biology.plan.engine.state.PhenotypePlanState;
import org.gentar.biology.plan.engine.state.ProductionPlanState;
import org.gentar.biology.project.Project;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private HistoryService<Project> historyService;

    public AssignmentStatusUpdater(
        ConflictsChecker conflictsChecker,
        AssignmentStatusService assignmentStatusService,
        HistoryService<Project> historyService)
    {
        this.conflictsChecker = conflictsChecker;
        this.assignmentStatusService = assignmentStatusService;
        this.historyService = historyService;
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
        if (!(projectHasStatusByName(project, AssignmentStatusNames.INACTIVE_STATUS_NAME)
            || projectHasStatusByName(project, AssignmentStatusNames.ASSIGNED_STATUS_NAME)))
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
        AssignmentStatus currentAssignmentStatus = originalProject.getAssignmentStatus();
        if (!currentAssignmentStatus.getName().equals(assignmentStatus.getName()))
        {
            newProject.setAssignmentStatus(assignmentStatus);
            registerAssignmentStatusStamp(newProject);
            saveProjectChangeInHistory(originalProject, newProject);
        }
    }

    private void saveProjectChangeInHistory(Project originalProject, Project newProject)
    {
        History history =
            historyService.detectTrackOfChanges(
                originalProject, newProject, originalProject.getId());
        history = historyService.filterDetailsInNestedEntity(history, "assignmentStatus", "name");
        historyService.saveTrackOfChanges(history);
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
            areAllPlansAborted = plans.stream().allMatch(this::planIsAborted);
        }
        if (areAllPlansAborted)
        {
            inactivateProject(project);
        }
        else if (projectIsInactive(project))
        {
            reactivateProject(project);
        }
    }

    private boolean planIsAborted(Plan plan)
    {
        String statusName = plan.getStatus().getName();
        return statusName.equals(ProductionPlanState.AttemptAborted.getInternalName()) ||
            statusName.equals(BreedingPlanState.BreedingAborted.getInternalName()) ||
            statusName.equals(PhenotypePlanState.PhenotypeProductionAborted.getInternalName()) ||
            statusName.equals(
                LateAdultPhenotypePlanState.LateAdultPhenotypeProductionAborted.getInternalName());
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

    private boolean projectIsInactive(Project project)
    {
        boolean result = false;
        AssignmentStatus assignmentStatus = project.getAssignmentStatus();
        if (assignmentStatus != null)
        {
            result = AssignmentStatusNames.INACTIVE_STATUS_NAME.equals(assignmentStatus.getName());
        }
        return result;
    }

    private boolean projectHasStatusByName(Project project, String assignmentStatusName)
    {
        boolean result = false;
        AssignmentStatus assignmentStatus = project.getAssignmentStatus();
        if (assignmentStatus != null)
        {
            result = assignmentStatusName.equals(assignmentStatus.getName());
        }
        return result;
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
