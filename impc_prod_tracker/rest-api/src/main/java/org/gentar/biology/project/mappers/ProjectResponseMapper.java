package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.biology.intention.ProjectIntentionResponseDTO;
import org.gentar.biology.intention.ProjectIntentionResponseMapper;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectConsortiumDTO;
import org.gentar.biology.project.ProjectController;
import org.gentar.biology.project.ProjectResponseDTO;
import org.gentar.biology.project.consortium.ProjectConsortiumMapper;
import org.gentar.biology.species.SpeciesMapper;
import org.gentar.biology.status.StatusStampMapper;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_unit.WorkUnit;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectResponseMapper implements Mapper<Project, ProjectResponseDTO>
{
    private final ProjectCommonDataMapper projectCommonDataMapper;
    private final StatusStampMapper statusStampMapper;
    private final ProjectIntentionResponseMapper projectIntentionResponseMapper;
    private final SpeciesMapper speciesMapper;
    private final ProjectConsortiumMapper projectConsortiumMapper;

    public ProjectResponseMapper(
        ProjectCommonDataMapper projectCommonDataMapper,
        StatusStampMapper statusStampMapper,
        ProjectIntentionResponseMapper projectIntentionResponseMapper,
        SpeciesMapper speciesMapper,
        ProjectConsortiumMapper projectConsortiumMapper)
    {
        this.projectCommonDataMapper = projectCommonDataMapper;
        this.statusStampMapper = statusStampMapper;
        this.projectIntentionResponseMapper = projectIntentionResponseMapper;
        this.speciesMapper = speciesMapper;
        this.projectConsortiumMapper = projectConsortiumMapper;
    }

    @Override
    public ProjectResponseDTO toDto(Project project)
    {
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        projectResponseDTO.setProjectCommonDataDTO(projectCommonDataMapper.toDto(project));
        projectResponseDTO.setTpn(project.getTpn());
        if (project.getAssignmentStatus() != null)
        {
            projectResponseDTO.setAssignmentStatusName(project.getAssignmentStatus().getName());
        }
        projectResponseDTO.setImitsMiPlan(project.getImitsMiPlan());
        if (project.getSummaryStatus() != null)
        {
            projectResponseDTO.setSummaryStatusName(project.getSummaryStatus().getName());
        }
        setStatusStampsDTO(projectResponseDTO, project);
        setProjectIntentionsDTOS(projectResponseDTO, project);
        setRelatedWorkUnitsDTOS(projectResponseDTO, project);
        setRelatedWorkGroupsDTOS(projectResponseDTO, project);
        setConsortiaToDto(projectResponseDTO, project);
        setSpeciesToDto(projectResponseDTO, project);
        addSelfLink(projectResponseDTO, project);
        return projectResponseDTO;
    }

    private void setStatusStampsDTO(ProjectResponseDTO projectResponseDTO, Project project)
    {
        List<StatusStampsDTO> statusStampsDTOS =
            statusStampMapper.toDtos(project.getAssignmentStatusStamps());
        statusStampsDTOS.sort(Comparator.comparing(StatusStampsDTO::getDate));
        projectResponseDTO.setStatusStampsDTOS(statusStampsDTOS);
    }

    private void setProjectIntentionsDTOS(ProjectResponseDTO projectResponseDTO, Project project)
    {
        List<ProjectIntentionResponseDTO> projectIntentionDTOs =
            projectIntentionResponseMapper.toDtos(project.getProjectIntentions());
        projectResponseDTO.setProjectIntentionDTOS(projectIntentionDTOs);
    }

    private void setRelatedWorkUnitsDTOS(ProjectResponseDTO projectResponseDTO, Project project)
    {
        List<String> relatedWorkUnits = new ArrayList<>();
        List<WorkUnit> workUnits = project.getRelatedWorkUnits();
        if (workUnits != null)
        {
            workUnits.forEach(x -> relatedWorkUnits.add(x.getName()));
        }
        projectResponseDTO.setRelatedWorkUnitNames(new HashSet<>(relatedWorkUnits));
    }

    private void setRelatedWorkGroupsDTOS(ProjectResponseDTO projectResponseDTO, Project project)
    {
        List<String> relatedWorkGroups = new ArrayList<>();
        List<WorkGroup> workGroups = project.getRelatedWorkGroups();
        if (workGroups != null)
        {
            workGroups.forEach(x -> relatedWorkGroups.add(x.getName()));
        }
        projectResponseDTO.setRelatedWorkGroupNames(new HashSet<>(relatedWorkGroups));
    }

    private void setConsortiaToDto(ProjectResponseDTO projectResponseDTO, Project project)
    {
        List<ProjectConsortiumDTO> projectConsortiumDTOS =
            projectConsortiumMapper.toDtos(project.getProjectConsortia());
        projectResponseDTO.setProjectConsortiumDTOS(projectConsortiumDTOS);
    }

    private void setSpeciesToDto(ProjectResponseDTO projectResponseDTO, Project project)
    {
        List<String> speciesNames = speciesMapper.toDtos(project.getSpecies());
        projectResponseDTO.setSpeciesNames(speciesNames);
    }

    private void addSelfLink(ProjectResponseDTO projectResponseDTO, Project project)
    {
        Link link = linkTo(methodOn(ProjectController.class)
            .findOne(project.getTpn()))
            .withSelfRel();
        projectResponseDTO.add(link);
    }

    @Override
    public Project toEntity(ProjectResponseDTO dto)
    {
        // We don't need to convert a project response into a entity. The ProjectResponseDTO is meant
        // only to show a response: the transformation of a plan entity into a dto that the user
        // can see.
        return null;
    }
}
