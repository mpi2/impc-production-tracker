package org.gentar.biology.project;

import org.gentar.Mapper;
import org.gentar.helpers.ProjectCsvRecord;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectCsvRecordMapper implements Mapper<Project, ProjectCsvRecord>
{
    private ProjectService projectService;
    private static final String SEPARATOR = ",";

    public ProjectCsvRecordMapper(ProjectService projectService)
    {
        this.projectService = projectService;
    }

    @Override
    public ProjectCsvRecord toDto(Project project)
    {
        ProjectCsvRecord projectCsvRecord = new ProjectCsvRecord();
        projectCsvRecord.setTpn(project.getTpn());
        projectCsvRecord.setExternalReference(project.getProjectExternalRef());
        projectCsvRecord.setGeneSymbolOrLocation(
            String.join(SEPARATOR, getSymbolsOrLocations(project)));
        projectCsvRecord.setAccIds(
            String.join(SEPARATOR, projectService.getAccIdsByProject(project)));
        projectCsvRecord.setAlleleIntentions(
            String.join(SEPARATOR, getIntentionAlleleTypeNames(project)));

        return projectCsvRecord;
    }

    private List<String> getSymbolsOrLocations(Project project)
    {
        List<String> targetNames = new ArrayList<>();
        var projectIntentionGenes = projectService.getIntentionGenesByProject(project);
        projectIntentionGenes.forEach(x -> targetNames.add(x.getGene().getSymbol()));
        return targetNames;
    }

    private List<String> getIntentionAlleleTypeNames(Project project)
    {
        List<String> alleleTypeNames = new ArrayList<>();
        var projectIntentionGenes = project.getProjectIntentions();
        if (projectIntentionGenes != null)
        {
            projectIntentionGenes.forEach(x -> alleleTypeNames.add(x.getAlleleType().getName()));
        }
        return alleleTypeNames;
    }
}
