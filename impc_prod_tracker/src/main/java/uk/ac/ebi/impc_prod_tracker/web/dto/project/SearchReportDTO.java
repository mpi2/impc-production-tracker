package uk.ac.ebi.impc_prod_tracker.web.dto.project;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.impc_prod_tracker.common.types.FilterTypes;
import uk.ac.ebi.impc_prod_tracker.service.project.search.SearchResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Representation of a Search Report.
 */
@Data
public class SearchReportDTO
{
    private LocalDateTime date;
    private String speciesName;
    private String searchTypeName;
    private Pageable pageable;
    private List<String> inputs;
    private Map<FilterTypes, List<String>> filters;
    private List<SearchResult> results;
}
