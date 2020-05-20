package org.gentar.biology.project.mappers;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectCommonDataDTO;
import org.gentar.biology.project.ProjectConsortiumDTO;
import org.gentar.biology.project.consortium.ProjectConsortium;
import org.gentar.biology.project.consortium.ProjectConsortiumMapper;
import org.gentar.biology.project.mappers.PrivacyMapper;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.biology.species.Species;
import org.gentar.biology.species.SpeciesMapper;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class to convert a ProjectCommonDataDTO into a Project object.
 */
@Component
public class ProjectCommonDataMapper implements Mapper<Project, ProjectCommonDataDTO>
{
    private EntityMapper entityMapper;
    private PrivacyMapper privacyMapper;
    private SpeciesMapper speciesMapper;
    private ProjectConsortiumMapper projectConsortiumMapper;

    public ProjectCommonDataMapper(
        EntityMapper entityMapper,
        PrivacyMapper privacyMapper,
        SpeciesMapper speciesMapper,
        ProjectConsortiumMapper projectConsortiumMapper)
    {
        this.entityMapper = entityMapper;
        this.privacyMapper = privacyMapper;
        this.speciesMapper = speciesMapper;
        this.projectConsortiumMapper = projectConsortiumMapper;
    }

    @Override
    public ProjectCommonDataDTO toDto(Project entity)
    {
        ProjectCommonDataDTO projectCommonDataDTO =
            entityMapper.toTarget(entity, ProjectCommonDataDTO.class);
        setConsortiaToDto(entity, projectCommonDataDTO);
        setSpeciesToDto(entity, projectCommonDataDTO);
        return projectCommonDataDTO;
    }

    private void setConsortiaToDto(Project project, ProjectCommonDataDTO projectCommonDataDTO)
    {
        List<ProjectConsortiumDTO> projectConsortiumDTOS =
            projectConsortiumMapper.toDtos(project.getProjectConsortia());
        projectCommonDataDTO.setProjectConsortiumDTOS(projectConsortiumDTOS);
    }

    private void setSpeciesToDto(Project project, ProjectCommonDataDTO projectCommonDataDTO)
    {
        List<String> speciesNames = speciesMapper.toDtos(project.getSpecies());
        projectCommonDataDTO.setSpeciesNames(speciesNames);
    }

    @Override
    public Project toEntity(ProjectCommonDataDTO projectCommonDataDTO)
    {
        Project project = entityMapper.toTarget(projectCommonDataDTO, Project.class);
        setConsortiaToEntity(project, projectCommonDataDTO);
        setPrivacyToEntity(project, projectCommonDataDTO);
        setSpeciesToEntity(project, projectCommonDataDTO);
        return project;
    }

    private void setConsortiaToEntity(Project project, ProjectCommonDataDTO projectCommonDataDTO)
    {
        Set<ProjectConsortium> projectConsortia = new HashSet<>(
            projectConsortiumMapper.toEntities(projectCommonDataDTO.getProjectConsortiumDTOS()));
        projectConsortia.forEach(x -> x.setProject(project));
        project.setProjectConsortia(projectConsortia);
    }

    private void setSpeciesToEntity(Project project, ProjectCommonDataDTO projectCommonDataDTO)
    {
        Set<Species> species =
            new HashSet<>(speciesMapper.toEntities(projectCommonDataDTO.getSpeciesNames()));
        project.setSpecies(species);
    }

    private void setPrivacyToEntity(Project project, ProjectCommonDataDTO projectCommonDataDTO)
    {
        String privacyName = projectCommonDataDTO.getPrivacyName();
        if (privacyName != null)
        {
            Privacy privacy = privacyMapper.toEntity(projectCommonDataDTO.getPrivacyName());
            project.setPrivacy(privacy);
        }
    }
}
