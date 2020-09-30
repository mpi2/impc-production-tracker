package org.gentar.biology.project.engine;

import org.gentar.biology.project.Project;
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

    public ProjectValidator(
        ContextAwarePolicyEnforcement policyEnforcement,
        ResourceAccessChecker<Project> resourceAccessChecker)
    {
        this.policyEnforcement = policyEnforcement;
        this.resourceAccessChecker = resourceAccessChecker;
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
