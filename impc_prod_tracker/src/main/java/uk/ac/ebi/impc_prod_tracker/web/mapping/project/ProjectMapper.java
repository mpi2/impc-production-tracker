package uk.ac.ebi.impc_prod_tracker.web.mapping.project;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_gene.ProjectIntentionGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_location.ProjectIntentionLocation;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_location.SortByProjectIntentionLocationIndex;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_sequence.ProjectIntentionSequence;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_sequence.SortByProjectIntentionSequenceIndex;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.species.SpeciesDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.status_stamps.StatusStampsDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.gene.ProjectIntentionGeneMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.location.ProjectIntentionLocationMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.sequence.ProjectIntentionSequenceMapper;
import uk.ac.ebi.impc_prod_tracker.web.mapping.statusStamp.StatusStampMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProjectMapper
{
    private ModelMapper modelMapper;
    private StatusStampMapper statusStampMapper;
    private ProjectIntentionGeneMapper projectIntentionGeneMapper;
    private ProjectIntentionLocationMapper projectIntentionLocationMapper;
    private ProjectIntentionSequenceMapper projectIntentionSequenceMapper;
    private static final String MGI_URL = "http://www.mousephenotype.org/data/genes/";

    public ProjectMapper(
        ModelMapper modelMapper,
        StatusStampMapper statusStampMapper,
        ProjectIntentionGeneMapper projectIntentionGeneMapper,
        ProjectIntentionLocationMapper projectIntentionLocationMapper,
        ProjectIntentionSequenceMapper projectIntentionSequenceMapper)
    {
        this.modelMapper = modelMapper;
        this.statusStampMapper = statusStampMapper;
        this.projectIntentionGeneMapper = projectIntentionGeneMapper;
        this.projectIntentionLocationMapper = projectIntentionLocationMapper;
        this.projectIntentionSequenceMapper = projectIntentionSequenceMapper;
    }

    public ProjectDTO toDto(Project project)
    {
        ProjectDTO projectDTO = null;
        if (project != null)
        {
            projectDTO = modelMapper.map(project, ProjectDTO.class);
            addStatusStampsDTO(project, projectDTO);
            addProjectIntentionGenes(project, projectDTO);
            addProjectIntentionLocations(project, projectDTO);
            addProjectIntentionSequences(project, projectDTO);
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

    private void addProjectIntentionGenes(Project project, ProjectDTO projectDTO)
    {
        Set<ProjectIntentionGene> projectIntentionGenes =
            getProjectIntentionGene(project.getProjectIntentions());
        if (projectIntentionGenes != null)
        {
            projectDTO.setProjectIntentionGeneDTOS(
                projectIntentionGeneMapper.toDtos(projectIntentionGenes));
        }
    }

    private void addProjectIntentionLocations(Project project, ProjectDTO projectDTO)
    {
        List<ProjectIntentionLocation> projectIntentionLocations =
            getProjectIntentionLocation(project.getProjectIntentions());
        if (projectIntentionLocations != null)
        {
            projectDTO.setProjectIntentionLocationDTOS(
                projectIntentionLocationMapper.toDtos(projectIntentionLocations));
        }
    }

    private void addProjectIntentionSequences(Project project, ProjectDTO projectDTO)
    {
        List<ProjectIntentionSequence> projectIntentionSequences =
            getProjectIntentionSequence(project.getProjectIntentions());
        if (projectIntentionSequences != null)
        {
            projectDTO.setProjectIntentionSequenceDTOS(
                projectIntentionSequenceMapper.toDtos(projectIntentionSequences));
        }
    }

    private Set<ProjectIntentionGene> getProjectIntentionGene(Set<ProjectIntention> projectIntentions)
    {
        Set<ProjectIntentionGene> projectIntentionGenes = null;
        if (projectIntentions != null)
        {
            projectIntentionGenes = projectIntentions.stream()
                .map(ProjectIntention::getProjectIntentionGene)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        }
        if (projectIntentionGenes.isEmpty())
        {
            projectIntentionGenes = null;
        }
        return projectIntentionGenes;
    }

    private List<ProjectIntentionLocation> getProjectIntentionLocation(Set<ProjectIntention> projectIntentions)
    {
        List<ProjectIntentionLocation> projectIntentionLocation = null;
        if (projectIntentions != null)
        {
            projectIntentionLocation = projectIntentions.stream()
                .map(ProjectIntention::getProjectIntentionLocation)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }
        Collections.sort(projectIntentionLocation, new SortByProjectIntentionLocationIndex());
        if (projectIntentionLocation.isEmpty())
        {
            projectIntentionLocation = null;
        }
        return projectIntentionLocation;
    }

    private List<ProjectIntentionSequence> getProjectIntentionSequence(
        Set<ProjectIntention> projectIntentions)
    {
        List<ProjectIntentionSequence> projectIntentionSequence = null;
        if (projectIntentions != null)
        {
            projectIntentionSequence = projectIntentions.stream()
                .map(ProjectIntention::getProjectIntentionSequence)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }
        Collections.sort(projectIntentionSequence, new SortByProjectIntentionSequenceIndex());
        if (projectIntentionSequence.isEmpty())
        {
            projectIntentionSequence = null;
        }
        return projectIntentionSequence;
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
