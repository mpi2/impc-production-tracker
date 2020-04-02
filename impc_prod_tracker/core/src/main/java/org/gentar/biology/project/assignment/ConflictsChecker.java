package org.gentar.biology.project.assignment;

import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectIntentionService;
import org.gentar.biology.project.ProjectQueryHelper;
import org.gentar.biology.status.StatusNames;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class checks the conflict status a project should have. The conflict status are a subset
 * of the possible assignment statues.
 * At the moment they are:
 *     * Assigned (1).
 *     * Inspect - GLT Mouse (2).
 *     * Inspect - Attempt (3).
 *     * Inspect - Conflict (4).
 *     * Conflict (5).
 *
 *     The numbers are the evaluation order of the statuses. Once the conditions are met to assign
 *     a status, the process stops. Each method assumes the previous methods where already evaluated
 *     so no logic is repeated.
 */
@Component
public class ConflictsChecker
{
    private AssignmentStatusService assignmentStatusService;
    private ProjectIntentionService projectIntentionService;
    private ProjectQueryHelper projectQueryHelper;

    private static final String DELETION_MUTATION_TYPE_NAME = "Deletion";

    public ConflictsChecker(
        AssignmentStatusService assignmentStatusService,
        ProjectIntentionService projectIntentionService,
        ProjectQueryHelper projectQueryHelper)
    {
        this.assignmentStatusService = assignmentStatusService;
        this.projectIntentionService = projectIntentionService;
        this.projectQueryHelper = projectQueryHelper;
    }

    /**
     * Checks if there is a conflict for the current project in the system. The conflicts
     * exist when there are several projects trying to do the same work in a gene (currently
     * contemplating only deletion).
     * The current Conflict Statuses are:
     * - Assigned.
     * - Inspect - GLT Mouse.
     * - Inspect - Attempt.
     * - Inspect - Conflict.
     * - Conflict.
     * @param project The project to evaluate
     * @return A {@link AssignmentStatus} referring to a conflict status if there was conflict
     * or Assigned if there was not conflict.
     */
    public AssignmentStatus checkConflict(Project project)
    {
        String assignmentStatusName;
        List<Project> projectConflicts = getProjectsSameDeletionIntention(project);
        if (validToAssigned(project, projectConflicts))
        {
            assignmentStatusName = AssignmentStatusNames.ASSIGNED_STATUS_NAME;
        }
        else if (validToInspectGltMouse(project, projectConflicts))
        {
            assignmentStatusName = AssignmentStatusNames.INSPECT_GLT_MOUSE_STATUS_NAME;
        }
        else if (validToInspectAttempt(project, projectConflicts))
        {
            assignmentStatusName = AssignmentStatusNames.INSPECT_ATTEMPT_STATUS_NAME;
        }
        else if (validToInspectConflict(project, projectConflicts))
        {
            assignmentStatusName = AssignmentStatusNames.INSPECT_CONFLICT_STATUS_NAME;
        }
        else
        {
            assignmentStatusName = AssignmentStatusNames.CONFLICT_STATUS_NAME;
        }
        return assignmentStatusService.getAssignmentStatusByName(assignmentStatusName);
    }

    /**
     * A project is set to [Assigned] if any of the following conditions are met:
     * 1) The intention includes >1 gene or the Molecular mutation != “deletion”.
     * 2) No other project with the same intention being:
     *      - 1 gene.
     *      - Molecular mutation => “deletion”.
     * @param project The project being evaluated.
     * @param projectConflicts
     * @return True or False depending if the project can be set to [Assigned] or not.
     */
    private boolean validToAssigned(Project project, List<Project> projectConflicts)
    {
        boolean result = false;
        var intentionGenes = projectQueryHelper.getIntentionGenesByProject(project);
        if (canBeAssignedDirectly(intentionGenes))
        {
            result = true;
        }
        else if (canBeAssigned(project, projectConflicts))
        {
            result = true;
        }
        return result;
    }

    private boolean canBeAssignedDirectly(List<ProjectIntentionGene> intentionGenes)
    {
        boolean result = false;
        if (intentionGenes != null)
        {
            result = intentionGenes.size() > 1 || !anyGeneIntentionIsDeletion(intentionGenes);
        }
        return result;
    }

    private boolean canBeAssigned(Project project, List<Project> projectConflicts)
    {
        List<Project> assignedProjectsWithDeletionIntention =
            projectConflicts.stream()
                .filter(x -> AssignmentStatusNames.ASSIGNED_STATUS_NAME.equals(
                    x.getAssignmentStatus().getName()))
                .collect(Collectors.toList());
        return assignedProjectsWithDeletionIntention.isEmpty();
    }

    /**
     * A project is set to [Inspect - GLT Mouse] if any of the following conditions are met:
     * 1) At least another project with intention being:
     *  - 1 gene.
     *  - Molecular mutation => “deletion”.
     *  - And that have at least one plan/attempt with status => "Genotype confirmed".
     *  This method only validates the last part of the condition because the rest is validated by
     *  the method called before this.
     * @param project The project being evaluated.
     * @param projectConflicts
     * @return True or False depending if the project can be set to [Inspect - GLT Mouse] or not.
     */
    private boolean validToInspectGltMouse(Project project, List<Project> projectConflicts)
    {
        boolean result = false;
        for (Project conflict : projectConflicts)
        {
            Set<Plan> plans = conflict.getPlans();
            result = plans.stream()
                .anyMatch(p -> StatusNames.GENOTYPE_CONFIRMED.equals(p.getStatus().getName()));
            if (result)
            {
                break;
            }
        }
        return result;
    }

    /**
     * A project is set to [Inspect - Attempt] if any of the following conditions are met:
     * 1) At least another project with intention being:
     *  - 1 gene.
     *  - Molecular mutation => “deletion”.
     *  - Have at least one plan/attempt with status != ("Attempt aborted" or "Genotype confirmed").
     *  This method only validates the last part of the condition because the rest is validated by
     *  the method called before this.
     * @param project The project being evaluated.
     * @param projectConflicts
     * @return True or False depending if the project can be set to [Inspect - Attempt] or not.
     */
    private boolean validToInspectAttempt(Project project, List<Project> projectConflicts)
    {
        boolean result = false;
        for (Project conflict : projectConflicts)
        {
            Set<Plan> plans = conflict.getPlans();
            result = plans.stream()
                .anyMatch(p ->
                    !StatusNames.ATTEMPT_ABORTED.equals(p.getStatus().getName()) &&
                        !StatusNames.GENOTYPE_CONFIRMED.equals(p.getStatus().getName()));
            if (result)
            {
                break;
            }
        }
        return result;
    }

    /**
     * A project is set to [Inspect - Attempt] if any of the following conditions are met:
     * 1) At least another project with intention being:
     *      - 1 gene.
     *      - Molecular mutation => “deletion”.
     *  This method only validates the last part of the condition because the rest is validated by
     *  the method called before this.
     * @param project The project being evaluated.
     * @param projectConflicts
     * @return True or False depending if the project can be set to [Inspect - Attempt] or not.
     */
    private boolean validToInspectConflict(Project project, List<Project> projectConflicts)
    {
        return projectConflicts.stream()
            .anyMatch(x -> AssignmentStatusNames.ASSIGNED_STATUS_NAME
                .equals( x.getAssignmentStatus().getName()));
    }

    /**
     * Get other projects with the same deletion intention as the project parameter.
     * @param project The reference project.
     * @return A list of projects with the same deletion intention as the project in parameter.
     */
    public List<Project> getProjectsSameDeletionIntention(Project project)
    {
        List<Project> projectsWithSameGeneIntentions =
            projectIntentionService.getProjectsWithSameGeneIntention(project);
        List<ProjectIntention> projectIntentions = new ArrayList<>();

        projectsWithSameGeneIntentions.forEach(
            x -> projectIntentions.addAll(x.getProjectIntentions().stream()
                .filter(i -> i.getMolecularMutationType().getName().equals(DELETION_MUTATION_TYPE_NAME))
                .collect(Collectors.toList())));
        return projectIntentions.stream()
            .map(ProjectIntention::getProject).collect(Collectors.toList());
    }

    private boolean anyGeneIntentionIsDeletion(List<ProjectIntentionGene> intentionGenes)
    {
        boolean result = false;
        if (intentionGenes != null)
        {
            result = intentionGenes.stream()
                .anyMatch(x -> isGeneIntentionADeletion(x.getProjectIntention()));
        }
        return result;
    }

    private boolean isGeneIntentionADeletion(ProjectIntention projectIntention)
    {
        return DELETION_MUTATION_TYPE_NAME.equals(
            projectIntention.getMolecularMutationType().getName());
    }

}
