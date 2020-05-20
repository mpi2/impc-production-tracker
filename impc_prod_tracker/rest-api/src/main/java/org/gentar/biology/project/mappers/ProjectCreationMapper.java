package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.biology.intention.ProjectIntentionMapper;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectCreationDTO;
import org.gentar.biology.project.mappers.ProjectCommonDataMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ProjectCreationMapper implements Mapper<Project, ProjectCreationDTO>
{
    private ProjectCommonDataMapper projectCommonDataMapper;
    private ProjectIntentionMapper projectIntentionMapper;

    public ProjectCreationMapper(
        ProjectCommonDataMapper projectCommonDataMapper,
        ProjectIntentionMapper projectIntentionMapper)
    {
        this.projectCommonDataMapper = projectCommonDataMapper;
        this.projectIntentionMapper = projectIntentionMapper;
    }

    @Override
    public ProjectCreationDTO toDto(Project entity)
    {
        // Project Creation information is always a dto send by the user. We don't need this
        // conversion here.
        return null;
    }

    @Override
    public Project toEntity(ProjectCreationDTO projectCreationDTO)
    {
        Project project =
            projectCommonDataMapper.toEntity(projectCreationDTO.getProjectCommonDataDTO());
        setProjectIntentionsToEntity(project, projectCreationDTO);
        return project;
    }

    private void setProjectIntentionsToEntity(Project project, ProjectCreationDTO projectCreationDTO)
    {
        List<ProjectIntention> projectIntentions =
            projectIntentionMapper.toEntities(projectCreationDTO.getProjectIntentionDTOS());
        projectIntentions.forEach(x -> x.setProject(project));
        project.setProjectIntentions(projectIntentions);
    }
}
