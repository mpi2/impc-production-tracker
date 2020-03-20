package org.gentar.biology.project;

import org.springframework.stereotype.Component;

@Component
public class UpdateProjectRequestProcessor
{
    public Project getProjectToUpdate(Project project, ProjectDTO projectDTO)
    {
        project.setComment(projectDTO.getComment());
        return project;
    }
}
