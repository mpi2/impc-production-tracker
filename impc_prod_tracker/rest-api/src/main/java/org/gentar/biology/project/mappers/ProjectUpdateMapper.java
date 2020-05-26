package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class ProjectUpdateMapper implements Mapper<Project, ProjectUpdateDTO>
{
    private ProjectCommonDataMapper projectCommonDataMapper;

    public ProjectUpdateMapper(ProjectCommonDataMapper projectCommonDataMapper)
    {
        this.projectCommonDataMapper = projectCommonDataMapper;
    }

    @Override
    public ProjectUpdateDTO toDto(Project project)
    {
        ProjectUpdateDTO projectUpdateDTO = new ProjectUpdateDTO();
        projectUpdateDTO.setProjectCommonDataDTO(projectCommonDataMapper.toDto(project));
        projectUpdateDTO.setTpn(project.getTpn());
        return projectUpdateDTO;
    }

    @Override
    public Project toEntity(ProjectUpdateDTO projectUpdateDTO)
    {
        Project project = new Project();
        if (projectUpdateDTO != null)
        {
            project = projectCommonDataMapper.toEntity(projectUpdateDTO.getProjectCommonDataDTO());
            project.setTpn(projectUpdateDTO.getTpn());
        }
        return project;
    }
}
