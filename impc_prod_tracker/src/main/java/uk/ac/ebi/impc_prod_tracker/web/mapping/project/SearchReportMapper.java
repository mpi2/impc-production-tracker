package uk.ac.ebi.impc_prod_tracker.web.mapping.project;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.service.project.search.SearchReport;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.SearchReportDTO;

@Component
public class SearchReportMapper
{
    private ModelMapper modelMapper;

    public SearchReportMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    public SearchReportDTO toDto(SearchReport searchReport)
    {
        SearchReportDTO searchReportDTO = modelMapper.map(searchReport, SearchReportDTO.class);
        return searchReportDTO;
    }
}
