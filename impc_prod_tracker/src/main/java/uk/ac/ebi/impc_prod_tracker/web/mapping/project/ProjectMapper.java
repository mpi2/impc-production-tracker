package uk.ac.ebi.impc_prod_tracker.web.mapping.project;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.ProjectGeneDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.ProjectLocationDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.species.SpeciesDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.status_stamps.StatusStampsDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.gene.ProjectGeneMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.location.ProjectLocationMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.statusStamp.StatusStampMapper;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectMapper
{
    private ModelMapper modelMapper;
    private StatusStampMapper statusStampMapper;
    private ProjectGeneMapper projectGeneMapper;
    private ProjectLocationMapper projectLocationMapper;
    private static final String MGI_URL = "http://www.mousephenotype.org/data/genes/";

    public ProjectMapper(
        ModelMapper modelMapper,
        StatusStampMapper statusStampMapper,
        ProjectGeneMapper projectGeneMapper,
        ProjectLocationMapper projectLocationMapper)
    {
        this.modelMapper = modelMapper;
        this.statusStampMapper = statusStampMapper;
        this.projectGeneMapper = projectGeneMapper;
        this.projectLocationMapper = projectLocationMapper;
    }

    public ProjectDTO toDto(Project project)
    {
        ProjectDTO projectDTO = null;
        if (project != null)
        {
            projectDTO = modelMapper.map(project, ProjectDTO.class);
            addStatusStampsDTO(project, projectDTO);
            addProjectGeneDTO(project, projectDTO);
            addProjectLocationDTO(project, projectDTO);
            addSpeciesDTO(project, projectDTO);
            addConsortiaNames(project, projectDTO);
        }
        return projectDTO;
    }

    private void addConsortiaNames(Project project, ProjectDTO projectDTO)
    {
        List<String> consortiaNames = new ArrayList<>();
        if (project.getConsortia() != null)
        {
            project.getConsortia().forEach(x -> consortiaNames.add(x.getName()));
        }
        projectDTO.setConsortiaNames(consortiaNames);
    }

    private void addSpeciesDTO(Project project, ProjectDTO projectDTO)
    {
        List<SpeciesDTO> projectSpeciesDTOs = new ArrayList<>();
        if (project.getSpecies() != null)
        {
            project.getSpecies().forEach(x ->
            {
                SpeciesDTO speciesDTO = new SpeciesDTO();
                speciesDTO.setName(x.getName());
                speciesDTO.setTaxonId(x.getTaxonId());
                projectSpeciesDTOs.add(speciesDTO);
            });
        }
        projectDTO.setProjectSpeciesDTOs(projectSpeciesDTOs);
    }

    private void addProjectLocationDTO(Project project, ProjectDTO projectDTO)
    {
        if (project.getLocations() != null && !project.getLocations().isEmpty())
        {
            List<ProjectLocationDTO> projectLocationDTOS =
                projectLocationMapper.toDtos(project.getLocations());
            projectDTO.setProjectLocationDTOS(projectLocationDTOS);
        }
    }

    private void addProjectGeneDTO(Project project, ProjectDTO projectDTO)
    {
        if (project.getGenes() != null && !project.getGenes().isEmpty())
        {
            List<ProjectGeneDTO> projectGeneDTOS = projectGeneMapper.toDtos(project.getGenes());
            projectDTO.setProjectGeneDTOS(projectGeneDTOS);
        }
    }

    private void addStatusStampsDTO(Project project, ProjectDTO projectDTO)
    {
        if (project.getAssignmentStatusStamps() != null)
        {
            List<StatusStampsDTO> statusStampsDTOS =
                statusStampMapper.toDtos(project.getAssignmentStatusStamps());
            projectDTO.setStatusStampsDTOS(statusStampsDTOS);
        }
    }

    public List<ProjectDTO> toDtos(List<Project> project)
    {
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        project.forEach(p -> projectDTOList.add(toDto(p)));
        return projectDTOList;
    }
}
