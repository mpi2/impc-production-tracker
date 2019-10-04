package uk.ac.ebi.impc_prod_tracker.web.mapping.project;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.service.project.search.SearchReport;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.search.SearchReportDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.search.SearchResultDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchReportMapper
{
    private ModelMapper modelMapper;
    private SearchResultMapper searchResultMapper;

    public SearchReportMapper(ModelMapper modelMapper, SearchResultMapper searchResultMapper)
    {
        this.modelMapper = modelMapper;
        this.searchResultMapper = searchResultMapper;
    }

    public SearchReportDTO toDto(SearchReport searchReport)
    {
        SearchReportDTO searchReportDTO = modelMapper.map(searchReport, SearchReportDTO.class);
        List<SearchResultDTO> searchResultDTOS =
            searchReport.getResults().stream()
                .map(x -> searchResultMapper.toDto(x))
                .collect(Collectors.toList());
        searchReportDTO.setResults(searchResultDTOS);
        return searchReportDTO;
    }
}
