package uk.ac.ebi.impc_prod_tracker.web.dto.project.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import uk.ac.ebi.impc_prod_tracker.common.types.FilterTypes;
import uk.ac.ebi.impc_prod_tracker.service.project.search.SearchResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Representation of a Search Report.
 */
@Data
public class SearchReportDTO extends RepresentationModel
{
    private LocalDateTime date;
    private String speciesName;
    private String searchTypeName;
    private List<String> inputs;
    private Map<FilterTypes, List<String>> filters;
    private List<SearchResultDTO> results;

    @JsonProperty("page")
    private PagedModel.PageMetadata pageMetadata;
}
