package org.gentar.biology.project;

import org.gentar.Mapper;
import org.gentar.helpers.ProjectCsvRecord;
import org.springframework.stereotype.Component;

@Component
public class ProjectCsvRecordMapper implements Mapper<Project, ProjectCsvRecord>
{
    private ProjectQueryHelper projectQueryHelper;
    private static final String SEPARATOR = ",";

    public ProjectCsvRecordMapper(ProjectQueryHelper projectQueryHelper)
    {
        this.projectQueryHelper = projectQueryHelper;
    }

    @Override
    public ProjectCsvRecord toDto(Project project)
    {
        ProjectCsvRecord projectCsvRecord = new ProjectCsvRecord();
        projectCsvRecord.setTpn(project.getTpn());
        projectCsvRecord.setExternalReference(project.getProjectExternalRef());
        projectCsvRecord.setGeneSymbolOrLocation(
            String.join(SEPARATOR, projectQueryHelper.getSymbolsOrLocations(project)));
        projectCsvRecord.setAccIds(
            String.join(SEPARATOR, projectQueryHelper.getAccIdsByProject(project)));
        projectCsvRecord.setMutationIntentions(
            String.join(SEPARATOR, projectQueryHelper.getIntentionMolecularMutationTypeNames(project)));
        projectCsvRecord.setBestHumanOrthologs(
            String.join(SEPARATOR, projectQueryHelper.getBestOrthologsSymbols(project)));
        projectCsvRecord.setWorkUnits(
            String.join(SEPARATOR, projectQueryHelper.getWorkUnitsNames(project)));
        projectCsvRecord.setWorkGroups(
            String.join(SEPARATOR, projectQueryHelper.getWorkGroupsNames(project)));
        projectCsvRecord.setProjectAssignment(project.getAssignmentStatus().getName());
        projectCsvRecord.setProjectSummaryStatus(project.getSummaryStatus().getName());
        projectCsvRecord.setPrivacy(project.getPrivacy().getName());
        projectCsvRecord.setConsortia(
            String.join(SEPARATOR, projectQueryHelper.getConsortiaNames(project)));
        return projectCsvRecord;
    }
}
