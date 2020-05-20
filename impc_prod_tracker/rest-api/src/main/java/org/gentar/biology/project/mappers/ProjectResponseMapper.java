package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.biology.intention.ProjectIntentionDTO;
import org.gentar.biology.intention.ProjectIntentionMapper;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectResponseDTO;
import org.gentar.biology.status.StatusStampMapper;
import org.gentar.biology.status_stamps.StatusStampsDTO;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_unit.WorkUnit;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Component
public class ProjectResponseMapper implements Mapper<Project, ProjectResponseDTO>
{
    private ProjectCommonDataMapper projectCommonDataMapper;
    private StatusStampMapper statusStampMapper;
    private ProjectIntentionMapper projectIntentionMapper;

    public ProjectResponseMapper(
        ProjectCommonDataMapper projectCommonDataMapper,
        StatusStampMapper statusStampMapper,
        ProjectIntentionMapper projectIntentionMapper)
    {
        this.projectCommonDataMapper = projectCommonDataMapper;
        this.statusStampMapper = statusStampMapper;
        this.projectIntentionMapper = projectIntentionMapper;
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
        setStatusStampsDTO(project, projectResponseDTO);
        setProjectIntentionsDTOS(project, projectResponseDTO);
        setRelatedWorkUnitsDTOS(project, projectResponseDTO);
        setRelatedWorkGroupsDTOS(project, projectResponseDTO);
        return projectResponseDTO;
    }

    private void setStatusStampsDTO(Project project, ProjectResponseDTO projectResponseDTO)
    {
        List<StatusStampsDTO> statusStampsDTOS =
            statusStampMapper.toDtos(project.getAssignmentStatusStamps());
        statusStampsDTOS.sort(Comparator.comparing(StatusStampsDTO::getDate));
        projectResponseDTO.setStatusStampsDTOS(statusStampsDTOS);
    }

    private void setProjectIntentionsDTOS(Project project, ProjectResponseDTO projectResponseDTO)
    {
        List<ProjectIntentionDTO> projectIntentionDTOs =
            projectIntentionMapper.toDtos(project.getProjectIntentions());
        projectResponseDTO.setProjectIntentionDTOS(projectIntentionDTOs);
    }

    private void setRelatedWorkUnitsDTOS(Project project, ProjectResponseDTO projectResponseDTO)
    {
        List<String> relatedWorkUnits = new ArrayList<>();
        List<WorkUnit> workUnits = project.getRelatedWorkUnits();
        if (workUnits != null)
        {
            workUnits.forEach(x -> relatedWorkUnits.add(x.getName()));
        }
        projectResponseDTO.setRelatedWorkUnitNames(new HashSet<>(relatedWorkUnits));
    }

    private void setRelatedWorkGroupsDTOS(Project project, ProjectResponseDTO projectResponseDTO)
    {
        List<String> relatedWorkGroups = new ArrayList<>();
        List<WorkGroup> workGroups = project.getRelatedWorkGroups();
        if (workGroups != null)
        {
            workGroups.forEach(x -> relatedWorkGroups.add(x.getName()));
        }
        projectResponseDTO.setRelatedWorkGroupNames(new HashSet<>(relatedWorkGroups));
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
