package org.gentar.biology.project.engine;

import org.gentar.biology.project.Project;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.security.permissions.PermissionService;
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

    public static final String READ_PROJECT_ACTION = "READ_PROJECT";

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

    public void validateReadPermissions(Project project)
    {
        try
        {
            policyEnforcement.checkPermission(project, READ_PROJECT_ACTION);
        }
        catch (AccessDeniedException ade)
        {
            throw new ForbiddenAccessException(
                Actions.READ, Project.class.getSimpleName(), project.getTpn());
        }
    }

    public Project getAccessChecked(Project project)
    {
        return (Project) resourceAccessChecker.checkAccess(project, READ_PROJECT_ACTION);
    }

    public List<Project> getCheckedCollection(Collection<Project> projects)
    {
        return projects.stream()
            .map(this::getAccessChecked).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
