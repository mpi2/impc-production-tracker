package org.gentar.biology.project.helper;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.biology.project.ProjectService;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.Project;

@Component
public class ProjectUtilities
{
    private static final String PROJECT_NOT_FOUND_ERROR = "Project %s does not exist.";
    private static ProjectService projectService;

    public ProjectUtilities(ProjectService projectService)
    {
        this.projectService = projectService;
    }

    public static Project getNotNullProjectByTpn(String tpn)
    {
        Project project = projectService.getProjectByTpn(tpn);
        if (project == null)
        {
            throw new UserOperationFailedException(
                String.format(PROJECT_NOT_FOUND_ERROR, tpn));
        }
        return project;
    }
}
