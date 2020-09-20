package org.gentar.biology.project.mappers;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectCommonDataDTO;
import org.gentar.biology.project.privacy.Privacy;
import org.springframework.stereotype.Component;

/**
 * Class to convert a ProjectCommonDataDTO into a Project object.
 */
@Component
public class ProjectCommonDataMapper implements Mapper<Project, ProjectCommonDataDTO>
{
    private EntityMapper entityMapper;
    private PrivacyMapper privacyMapper;

    public ProjectCommonDataMapper(
        EntityMapper entityMapper,
        PrivacyMapper privacyMapper)
    {
        this.entityMapper = entityMapper;
        this.privacyMapper = privacyMapper;
    }

    @Override
    public ProjectCommonDataDTO toDto(Project entity)
    {
        ProjectCommonDataDTO projectCommonDataDTO =
            entityMapper.toTarget(entity, ProjectCommonDataDTO.class);
        return projectCommonDataDTO;
    }

    @Override
    public Project toEntity(ProjectCommonDataDTO projectCommonDataDTO)
    {
        Project project = entityMapper.toTarget(projectCommonDataDTO, Project.class);
        setPrivacyToEntity(project, projectCommonDataDTO);
        return project;
    }

    private void setPrivacyToEntity(Project project, ProjectCommonDataDTO projectCommonDataDTO)
    {
        Privacy privacy = privacyMapper.toEntity(projectCommonDataDTO.getPrivacyName());
        project.setPrivacy(privacy);
    }
}
