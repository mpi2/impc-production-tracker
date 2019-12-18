package org.gentar.biology.project;

import org.gentar.Mapper;
import org.gentar.helpers.SearchCsvRecord;
import org.gentar.biology.project.search.SearchResult;
import org.springframework.stereotype.Component;

@Component
public class SearchCsvRecordMapper implements Mapper<SearchResult, SearchCsvRecord>
{
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
            searchCsvRecord.setPrivacy(project.getPrivacy().getName());
        }
        return searchCsvRecord;
    }
}
