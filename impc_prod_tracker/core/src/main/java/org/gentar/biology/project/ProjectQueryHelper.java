package org.gentar.biology.project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.ortholog.Ortholog;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.type.PlanTypeName;
import org.springframework.stereotype.Component;

/**
 * A class with common queries to a project
 */
@Component
public class ProjectQueryHelper
{

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

    public List<String> getAccIdsByProject(Project project)
    {
        List<String> accIds = new ArrayList<>();
        List<ProjectIntentionGene> projectIntentionGenes = getIntentionGenesByProject(project);
        projectIntentionGenes.forEach(x -> accIds.add(x.getGene().getAccId()));
        return accIds;
    }

    public List<Plan> getPlansByType(Project project, PlanTypeName planTypeName)
    {
        List<Plan> plansByType = new ArrayList<>();
        Set<Plan> allPlans = project.getPlans();
        if (allPlans != null)
        {
            plansByType = allPlans.stream()
                .filter(x -> x.getPlanType().getName().equals(planTypeName.getLabel()))
                .collect(Collectors.toList());
        }
        return plansByType;
    }

    public List<String> getBestOrthologsSymbols(Project project)
    {
        List<String> bestOrthologsSymbols = new ArrayList<>();
        List<ProjectIntentionGene> intentionGenes = getIntentionGenesByProject(project);
        if (!CollectionUtils.isEmpty(intentionGenes))
        {
            ProjectIntentionGene projectIntentionGene = intentionGenes.get(0);
            List<Ortholog> bestOrthologs = projectIntentionGene.getBestOrthologs();
            if (!CollectionUtils.isEmpty(bestOrthologs))
            {
                bestOrthologs.forEach(x -> bestOrthologsSymbols.add(x.getSymbol()));
            }
        }
        return bestOrthologsSymbols;
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

    public Set<String> getWorkUnitsNames(Project project)
    {
        Set<String> workUnitsNames = new HashSet<>();
        project.getRelatedWorkUnits().forEach(x -> workUnitsNames.add(x.getName()));
        return workUnitsNames;
    }

    public Set<String> getWorkGroupsNames(Project project)
    {
        Set<String> workGroupsNames = new HashSet<>();
        project.getRelatedWorkGroups().forEach(x -> workGroupsNames.add(x.getName()));
        return workGroupsNames;
    }

    public List<String> getConsortiaNames(Project project)
    {
        List<String> consortiaNames = new ArrayList<>();
        project.getRelatedConsortia().forEach(x -> consortiaNames.add(x.getName()));
        return consortiaNames;
    }
}
