package org.gentar.biology.project;

import org.gentar.Mapper;
import org.gentar.helpers.SearchCsvRecord;
import org.gentar.biology.project.search.SearchResult;
import org.springframework.stereotype.Component;

@Component
public class SearchCsvRecordMapper implements Mapper<SearchResult, SearchCsvRecord>
{
    private ProjectQueryHelper projectQueryHelper;
    private static final String SEPARATOR = ",";

    public SearchCsvRecordMapper(ProjectQueryHelper projectQueryHelper)
    {
        this.projectQueryHelper = projectQueryHelper;
    }

    @Override
    public SearchCsvRecord toDto(SearchResult searchResult)
    {
        SearchCsvRecord searchCsvRecord = new SearchCsvRecord();
        searchCsvRecord.setInput(searchResult.getInput());
        searchCsvRecord.setSearchComment(searchResult.getComment());
        Project project = searchResult.getProject();
        if (project != null)
        {
            searchCsvRecord.setTpn(project.getTpn());
            searchCsvRecord.setGeneSymbolOrLocation(
                String.join(SEPARATOR, projectQueryHelper.getSymbolsOrLocations(project)));
            searchCsvRecord.setAccIds(
                String.join(SEPARATOR, projectQueryHelper.getAccIdsByProject(project)));
            searchCsvRecord.setAlleleIntentions(
                String.join(SEPARATOR, projectQueryHelper.getIntentionAlleleTypeNames(project)));
            searchCsvRecord.setBestHumanOrthologs(
                String.join(SEPARATOR, projectQueryHelper.getBestOrthologsSymbols(project)));
            searchCsvRecord.setWorkUnits(
                String.join(SEPARATOR, projectQueryHelper.getWorkUnitsNames(project)));
            searchCsvRecord.setWorkGroups(
                String.join(SEPARATOR, projectQueryHelper.getWorkGroupsNames(project)));
            searchCsvRecord.setProjectAssignment(project.getAssignmentStatus().getName());
            searchCsvRecord.setPrivacy(project.getPrivacy().getName());
            searchCsvRecord.setConsortia(
                String.join(SEPARATOR, projectQueryHelper.getConsortiaNames(project)));
        }
        return searchCsvRecord;
    }
}
