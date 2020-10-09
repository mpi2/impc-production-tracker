package org.gentar.biology.project.engine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectQueryHelper;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.security.permissions.Operations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProjectValidator
{
    private final ContextAwarePolicyEnforcement policyEnforcement;
    private final ResourceAccessChecker<Project> resourceAccessChecker;
    private final ProjectQueryHelper projectQueryHelper;

    public ProjectValidator(
        ContextAwarePolicyEnforcement policyEnforcement,
        ResourceAccessChecker<Project> resourceAccessChecker,
        ProjectQueryHelper projectQueryHelper)
    {
        this.policyEnforcement = policyEnforcement;
        this.resourceAccessChecker = resourceAccessChecker;
        this.projectQueryHelper = projectQueryHelper;
    }

    public void validateData(Project project)
    {
        var plans = project.getPlans();
        if (plans == null)
        {
            throw new UserOperationFailedException("There are not plans associated with the project.");
        }
    }

    /**
     * Check if the current logged user has permission to create the project.
     * @param project Project being created.
     */
    public void validatePermissionToCreateProject(Project project)
    {
        if (!policyEnforcement.hasPermission(project, Actions.CREATE_PROJECT_ACTION))
        {
            throwPermissionExceptionForProject(Operations.CREATE, project);
        }
    }

    /**
     * Check if the current logged user has permission to update the project.
     * @param project Project being updated.
     */
    public void validatePermissionToUpdateProject(Project project)
    {
        if (!policyEnforcement.hasPermission(project, Actions.UPDATE_PROJECT_ACTION))
        {
            throwPermissionExceptionForProject(Operations.UPDATE, project);
        }
    }

    /**
     * Checks if a production plan can be created under this project.
     * A user can create a production plan only if they have modify access to the project.
     * @param project Project to validate
     */
    public void validatePermissionToAddProductionPlan(Project project)
    {
        try
        {
            validatePermissionToUpdateProject(project);
        }
        catch (ForbiddenAccessException e)
        {
            throw new ForbiddenAccessException(
                Operations.CREATE,
                Plan.class.getSimpleName(),
                null,
                e.getMessage());
        }
    }

    /**
     * Checks if a penotyping plan can be created under this project.
     * A user can create a phenotyping plan if they have read access to the project.
     * To create a phenotyping plan at least one finished production plan must exist first in the project.
     * @param project Project to validate
     */
    public void validatePermissionToAddPhenotypingPlan(Project project)
    {
        if (!policyEnforcement.hasPermission(project, Actions.READ_PROJECT_ACTION))
        {
            throw new ForbiddenAccessException(
                Operations.CREATE,
                Plan.class.getSimpleName(),
                null,
                "You do not have permission to edit the project [" + project + "].");
        }
        List<Plan> productionPlans =
            projectQueryHelper.getPlansByType(project, PlanTypeName.PRODUCTION);
        if (productionPlans.isEmpty())
        {
            throw new ForbiddenAccessException(
                Operations.CREATE,
                Plan.class.getSimpleName(),
                null,
                "At least one production plan is required.");
        }
    }

    private void throwPermissionExceptionForProject(Operations action, Project project)
    {
        String entityType = Project.class.getSimpleName();
        throw new ForbiddenAccessException(action, entityType, project.getTpn());
    }

    public void validateReadPermissions(Project project)
    {
        try
        {
            policyEnforcement.checkPermission(project, Actions.READ_PROJECT_ACTION);
        }
        catch (AccessDeniedException ade)
        {
            throw new ForbiddenAccessException(
                Operations.READ, Project.class.getSimpleName(), project.getTpn());
        }
    }

    public Project getAccessChecked(Project project)
    {
        return (Project) resourceAccessChecker.checkAccess(project, Actions.READ_PROJECT_ACTION);
    }

    public List<Project> getCheckedCollection(Collection<Project> projects)
    {
        return projects.stream()
            .map(this::getAccessChecked).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
