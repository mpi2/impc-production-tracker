package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.biology.intention.ProjectIntentionCreationMapper;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectCreationDTO;
import org.gentar.biology.project.consortium.ProjectConsortium;
import org.gentar.biology.project.consortium.ProjectConsortiumMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ProjectCreationMapper implements Mapper<Project, ProjectCreationDTO>
{
    private final ProjectCommonDataMapper projectCommonDataMapper;
    private final ProjectIntentionCreationMapper projectIntentionCreationMapper;
    private final ProjectConsortiumMapper projectConsortiumMapper;

    public ProjectCreationMapper(
        ProjectCommonDataMapper projectCommonDataMapper,
        ProjectIntentionCreationMapper projectIntentionCreationMapper,
        ProjectConsortiumMapper projectConsortiumMapper)
    {
        this.projectCommonDataMapper = projectCommonDataMapper;
        this.projectIntentionCreationMapper = projectIntentionCreationMapper;
        this.projectConsortiumMapper = projectConsortiumMapper;
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
        setConsortiaToEntity(project, projectCreationDTO);
        return project;
    }

    private void setProjectIntentionsToEntity(Project project, ProjectCreationDTO projectCreationDTO)
    {
        List<ProjectIntention> projectIntentions = new ArrayList<>(
            projectIntentionCreationMapper.toEntities(projectCreationDTO.getProjectIntentionCreationDTOS()));
        projectIntentions.forEach(x -> x.setProject(project));
        project.setProjectIntentions(projectIntentions);
    }

    private void setConsortiaToEntity(Project project, ProjectCreationDTO projectCreationDTO)
    {
        Set<ProjectConsortium> projectConsortia = new HashSet<>(
            projectConsortiumMapper.toEntities(projectCreationDTO.getProjectConsortiumDTOS()));
        projectConsortia.forEach(x -> x.setProject(project));
        project.setProjectConsortia(projectConsortia);
    }
}
