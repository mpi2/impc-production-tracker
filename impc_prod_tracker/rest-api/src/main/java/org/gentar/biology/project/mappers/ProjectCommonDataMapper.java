package org.gentar.biology.project.mappers;

import org.apache.logging.log4j.util.Strings;
import org.gentar.Mapper;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectCommonDataDTO;
import org.gentar.biology.project.esCellQc.ProjectEsCellQc;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.biology.project.project_es_cell_qc.ProjectEsCellQcDTO;
import org.springframework.stereotype.Component;

/**
 * Class to convert a ProjectCommonDataDTO into a Project object.
 */
@Component
public class ProjectCommonDataMapper implements Mapper<Project, ProjectCommonDataDTO>
{
    private final PrivacyMapper privacyMapper;
    private final ProjectEsCellQcMapper projectEsCellQcMapper;

    public ProjectCommonDataMapper(
            PrivacyMapper privacyMapper,
            ProjectEsCellQcMapper projectEsCellQcMapper)
    {
        this.privacyMapper = privacyMapper;
        this.projectEsCellQcMapper = projectEsCellQcMapper;
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

        ProjectEsCellQcDTO projectEsCellQcDTO = projectEsCellQcMapper.toDto(entity.getProjectEsCellQc());
        projectCommonDataDTO.setProjectEsCellQcDTO(projectEsCellQcDTO);
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
        setUpdateEsCellQc(project, projectCommonDataDTO);;
        return project;
    }

    private void setUpdateEsCellQc(Project project, ProjectCommonDataDTO projectCommonDataDTO)
    {
        if (projectCommonDataDTO.getProjectEsCellQcDTO() != null)
        {
            ProjectEsCellQc projectEsCellQc = projectEsCellQcMapper.toEntity(projectCommonDataDTO.getProjectEsCellQcDTO());
            project.setProjectEsCellQc(projectEsCellQc);
        }
    }

    private void setPrivacyToEntity(Project project, ProjectCommonDataDTO projectCommonDataDTO)
    {
        Privacy privacy = privacyMapper.toEntity(projectCommonDataDTO.getPrivacyName());
        project.setPrivacy(privacy);
    }
}
