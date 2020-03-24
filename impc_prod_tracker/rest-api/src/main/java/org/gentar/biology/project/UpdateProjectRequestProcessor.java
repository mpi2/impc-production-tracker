package org.gentar.biology.project;

import org.springframework.stereotype.Component;

@Component
public class UpdateProjectRequestProcessor
{
    /**
     * Takes a project and updates it with new information from a projectDTO.
     * It updates only the information that makes sense to update by the user.
     * @param project The project without changes.
     * @param projectDTO The changes to be done in project.
     * @return a project with the new information.
     */
    public Project getProjectToUpdate(Project project, ProjectDTO projectDTO)
    {
        project.setComment(projectDTO.getComment());
        project.setRecovery(projectDTO.getRecovery());
        return project;
    }
}
