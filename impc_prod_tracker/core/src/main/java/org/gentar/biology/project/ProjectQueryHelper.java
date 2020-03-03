package org.gentar.biology.project;

import org.gentar.biology.ortholog.Ortholog;
import org.gentar.biology.ortholog.OrthologService;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class with common queries to a project
 */
@Component
public class ProjectQueryHelper
{
    private OrthologService orthologService;

    public ProjectQueryHelper(OrthologService orthologService)
    {
        this.orthologService = orthologService;
    }

    public List<String> getBestOrthologsSymbols(Project project)
    {
        List<String> bestOrthologsAccIdsByProject = new ArrayList<>();
        List<String> accIds = getAccIdsByProject(project);
        Map<String, List<Ortholog>> orthologsByAccIds = orthologService.getOrthologsByAccIds(accIds);
        List<Ortholog> allOrthologs = new ArrayList<>();
        orthologsByAccIds.forEach( (k, v) -> allOrthologs.addAll(v));
        List<Ortholog> bestOrthologs = orthologService.calculateBestOrthologs(allOrthologs);
        bestOrthologs.forEach(x -> bestOrthologsAccIdsByProject.add(x.getSymbol()));
        return bestOrthologsAccIdsByProject;
    }

    public List<ProjectIntentionGene> getIntentionGenesByProject(Project project)
    {
        List<ProjectIntentionGene> projectIntentionGenes = new ArrayList<>();
        List<ProjectIntention> intentions = project.getProjectIntentions();
        if (intentions != null)
        {
            intentions.forEach(x -> {
                ProjectIntentionGene intentionGene = x.getProjectIntentionGene();
                if (intentionGene != null)
                {
                    projectIntentionGenes.add(intentionGene);
                }
            });
        }
        return projectIntentionGenes;
    }

    public List<String> getSymbolsOrLocations(Project project)
    {
        List<String> targetNames = new ArrayList<>();
        var projectIntentionGenes = getIntentionGenesByProject(project);
        projectIntentionGenes.forEach(x -> targetNames.add(x.getGene().getSymbol()));
        return targetNames;
    }

    public List<String> getIntentionMolecularMutationTypeNames(Project project)
    {
        List<String> molecularMutationTypeNames = new ArrayList<>();
        var projectIntentionGenes = project.getProjectIntentions();
        if (projectIntentionGenes != null)
        {
            projectIntentionGenes.forEach(x -> molecularMutationTypeNames.add(x.getMolecularMutationType().getName()));
        }
        return molecularMutationTypeNames;
    }

    public List<String> getAccIdsByProject(Project project)
    {
        List<String> accIds = new ArrayList<>();
        List<ProjectIntentionGene> projectIntentionGenes = getIntentionGenesByProject(project);
        projectIntentionGenes.forEach(x -> accIds.add(x.getGene().getAccId()));
        return accIds;
    }

    public Set<String> getWorkUnitsNames(Project project)
    {
        Set<String> workUnitsNames = new HashSet<>();
        project.getRelatedWorkUnits().forEach(x -> workUnitsNames.add(x.getName()));
        return workUnitsNames;
    }

    public Set<String> getWorkGroupsNames(Project project)
    {
        Set<String> workGroupsNames = new HashSet<>();
        project.getRelatedWorkUnits().forEach(x -> workGroupsNames.add(x.getName()));
        return workGroupsNames;
    }

    public List<String> getConsortiaNames(Project project)
    {
        List<String> consortiaNames = new ArrayList<>();
        project.getRelatedConsortia().forEach(x -> consortiaNames.add(x.getName()));
        return consortiaNames;
    }
}
