package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.biology.project.search.SearchReport;
import org.gentar.common.filters.FilterDTO;
import org.gentar.biology.project.search.SearchReportDTO;
import org.gentar.biology.project.search.SearchResultDTO;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SearchReportMapper implements Mapper<SearchReport, SearchReportDTO>
{
    private final SearchResultMapper searchResultMapper;

    public SearchReportMapper(SearchResultMapper searchResultMapper)
    {
        this.searchResultMapper = searchResultMapper;
    }

    @Override
    public SearchReportDTO toDto(SearchReport searchReport)
    {
        SearchReportDTO searchReportDTO = new SearchReportDTO();
        searchReportDTO.setDate(searchReport.getDate());
        searchReportDTO.setSearchTypeName(searchReport.getSearchType().getName());
        searchReportDTO.setInputs(searchReport.getInputs());
        searchReportDTO.setPageMetadata(searchReport.getPageMetadata());
        setSearchResultsDtos(searchReportDTO, searchReport);
        setFiltersDto(searchReportDTO, searchReport);
        return searchReportDTO;
    }

    private void setSearchResultsDtos(SearchReportDTO searchReportDTO, SearchReport searchReport)
    {
        List<SearchResultDTO> searchResultDTOS = searchResultMapper.toDtos(searchReport.getResults());
        searchReportDTO.setResults(searchResultDTOS);
    }

    private void setFiltersDto(SearchReportDTO searchReportDTO, SearchReport searchReport)
    {
        List<FilterDTO> filterDTOS = new ArrayList<>();
        if (searchReport.getFilters() != null)
        {
            searchReport.getFilters().forEach((key, value) -> filterDTOS.add(new FilterDTO(key, value)));
        }
        searchReportDTO.setFilters(filterDTOS);
    }

    private List<FilterDTO> toFilterDtos(Map<String, List<String>> filters)
    {
        List<FilterDTO> filterDTOS = new ArrayList<>();
        if (filters != null)
        {
            filters.forEach((key, value) -> filterDTOS.add(new FilterDTO(key, value)));
        }
        return filterDTOS;

    }
}
