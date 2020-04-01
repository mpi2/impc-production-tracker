package org.gentar.biology.project.assignment;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.state.BreedingPlanState;
import org.gentar.biology.plan.engine.state.LateAdultPhenotypePlanState;
import org.gentar.biology.plan.engine.state.PhenotypePlanState;
import org.gentar.biology.plan.engine.state.ProductionPlanState;
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

    public void setCalculatedAssignmentStatus(Project project)
    {
        AssignmentStatus currentAssignmentStatus = project.getAssignmentStatus();
        AssignmentStatus status = conflictsChecker.checkConflict(project);
        if (!currentAssignmentStatus.getName().equals(status.getName()))
        {
            project.setAssignmentStatus(status);
            registerAssignmentStatusStamp(project);
        }
    }

    /**
     * A project needs to be inactivated if all its plans are aborted.
     * @param project Project to evaluate if needs to be inactivated.
     */
    public void inactivateProjectIfNeeded(Project project)
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
            statusName.equals(LateAdultPhenotypePlanState.LateAdultPhenotypeProductionAborted.getInternalName());
    }

    /**
     * Inactivates a project, meaning the Assignment Status is inactive.
     * This method should be called
     * @param project The project to be inactivated.
     */
    private void inactivateProject(Project project)
    {
        AssignmentStatus inactive =
            assignmentStatusService.getAssignmentStatusByName(
                AssignmentStatusNames.INACTIVE_STATUS_NAME);
        project.setAssignmentStatus(inactive);
        registerAssignmentStatusStamp(project);
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

    /**
     * Given that a project was updated (its status of information in entities under it), then check
     * other projects that can br conflicting with it, searching for a potential change in that
     * conflict status (a new Assignment Status)
     * @param project Project to take as reference
     */
    public void updateConflictingProjects(Project project)
    {
        List<Project> conflictingProjects = conflictsChecker.getProjectsSameDeletionIntention(project);
        conflictingProjects.forEach(this::setCalculatedAssignmentStatus);
    }
}
