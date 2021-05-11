package org.gentar.biology.project.mappers;

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
    private PrivacyMapper privacyMapper;

    public ProjectCommonDataMapper(
        PrivacyMapper privacyMapper)
    {
        this.privacyMapper = privacyMapper;
    }

    @Override
    public ProjectCommonDataDTO toDto(Project entity)
    {
        ProjectCommonDataDTO projectCommonDataDTO = new ProjectCommonDataDTO();
        projectCommonDataDTO.setPrivacyName(entity.getPrivacy().getName());
        projectCommonDataDTO.setRecovery(entity.getRecovery());
        projectCommonDataDTO.setEsCellQcOnly(entity.getEsCellQcOnly());
        projectCommonDataDTO.setComment(entity.getComment());
        projectCommonDataDTO.setReactivationDate(entity.getReactivationDate());
        return projectCommonDataDTO;
    }

    @Override
    public Project toEntity(ProjectCommonDataDTO projectCommonDataDTO)
    {
        Project project = new Project();
        project.setRecovery(projectCommonDataDTO.getRecovery());
        project.setEsCellQcOnly(projectCommonDataDTO.getEsCellQcOnly());
        project.setComment(projectCommonDataDTO.getComment());
        project.setReactivationDate(projectCommonDataDTO.getReactivationDate());
        setPrivacyToEntity(project, projectCommonDataDTO);
        return project;
    }

    private void setPrivacyToEntity(Project project, ProjectCommonDataDTO projectCommonDataDTO)
    {
        Privacy privacy = privacyMapper.toEntity(projectCommonDataDTO.getPrivacyName());
        project.setPrivacy(privacy);
    }
}
