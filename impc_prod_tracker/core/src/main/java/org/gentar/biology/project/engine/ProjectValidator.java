package org.gentar.biology.project.engine;

import org.gentar.biology.project.Project;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.security.permissions.PermissionService;
import org.springframework.stereotype.Component;

@Component
public class ProjectValidator
{
    private final ContextAwarePolicyEnforcement policyEnforcement;

    public ProjectValidator(ContextAwarePolicyEnforcement policyEnforcement)
    {
        this.policyEnforcement = policyEnforcement;
    }

    public void validateData(Project project)
    {
        var plans = project.getPlans();
        if (plans == null)
        {
            throw new UserOperationFailedException(
                "There are not plans associated with the project.");
        }
    }

    /**
     * Check if the current logged user has permission to create the project.
     * @param project Project being created.
     */
    public void validatePermissionToCreateProject(Project project)
    {
        if (!policyEnforcement.hasPermission(project, PermissionService.CREATE_PROJECT_ACTION))
        {
            throwPermissionExceptionForProject(Actions.CREATE, project);
        }
    }

    /**
     * Check if the current logged user has permission to update the project.
     * @param project Project being updated.
     */
    public void validatePermissionToUpdateProject(Project project)
    {
        if (!policyEnforcement.hasPermission(project, PermissionService.UPDATE_PROJECT_ACTION))
        {
            throwPermissionExceptionForProject(Actions.UPDATE, project);
        }
    }

    private void throwPermissionExceptionForProject(Actions action, Project project)
    {
        String entityType = Project.class.getSimpleName();
        throw new ForbiddenAccessException(action, entityType, project.getTpn());
    }
}
