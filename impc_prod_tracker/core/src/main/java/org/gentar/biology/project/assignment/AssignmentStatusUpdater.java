package org.gentar.biology.project.assignment;

import org.gentar.biology.project.Project;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Class in charge of setting and updating the Assignment Status for a project depending on its
 * conditions.
 */
@Component
public class AssignmentStatusUpdater
{
    private AssignmentStatusService assignmentStatusService;
    private ConflictsChecker conflictsChecker;

    private static final AssignmentStatusAction WITHDRAW =
        new AssignmentStatusAction("withdraw", "Withdraw Project");

    public AssignmentStatusUpdater(
        AssignmentStatusService assignmentStatusService, ConflictsChecker conflictsChecker)
    {
        this.assignmentStatusService = assignmentStatusService;
        this.conflictsChecker = conflictsChecker;
    }

    public AssignmentStatus calculateAssignmentStatus(Project project)
    {
        String assignmentStatusName = null;
        if (tryingToSetNewStatusToWithdraw(project))
        {
            assignmentStatusName = AssignmentStatusNames.WITHDRAWN_STATUS_NAME;
        }
        if (assignmentStatusName == null)
        {
            return null;
        }
        else
        {
            return assignmentStatusService.getAssignmentStatusByName(assignmentStatusName);
        }
    }

    public AssignmentStatus checkConflict(Project project)
    {
        return conflictsChecker.checkConflict(project);
    }

    private boolean tryingToSetNewStatusToWithdraw(Project project)
    {
        boolean result = project.getWithdrawn();
        AssignmentStatus currentAssignmentStatus = project.getAssignmentStatus();
        if (currentAssignmentStatus != null)
        {
            result = !AssignmentStatusNames.WITHDRAWN_STATUS_NAME.equalsIgnoreCase(
                currentAssignmentStatus.getName());
        }
        return result;
    }

    private boolean tryingToReverseWithdraw(Project project)
    {
        boolean newWantedStatusIsWithdrawn = project.getWithdrawn();
        boolean currentStatusIsWithdraw = false;
        AssignmentStatus currentAssignmentStatus = project.getAssignmentStatus();
        if (currentAssignmentStatus != null)
        {
            currentStatusIsWithdraw = AssignmentStatusNames.WITHDRAWN_STATUS_NAME.equalsIgnoreCase(
                currentAssignmentStatus.getName());
        }
        return newWantedStatusIsWithdrawn && currentStatusIsWithdraw;
    }

    public List<AssignmentStatusAction> getPossibleAssignmentStatusActions(Project project)
    {
        List<AssignmentStatusAction> possibleActions = new ArrayList<>();
        AssignmentStatus currentStatus = project.getAssignmentStatus();
        String currentStatusName = null;
        if (currentStatus != null)
        {
            currentStatusName = currentStatus.getName();
        }
        assert currentStatusName != null;
        switch (currentStatusName)
        {
            case AssignmentStatusNames.ASSIGNED_STATUS_NAME:
            case AssignmentStatusNames.INSPECT_GLT_MOUSE_STATUS_NAME:
            case AssignmentStatusNames.INSPECT_ATTEMPT_STATUS_NAME:
            case AssignmentStatusNames.INSPECT_CONFLICT_STATUS_NAME:
            case AssignmentStatusNames.CONFLICT_STATUS_NAME:
                possibleActions.add(WITHDRAW);
                break;
        }
        return possibleActions;
    }
}
