package uk.ac.ebi.impc_prod_tracker.web.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.ProjectIntentionDTO;

@Data
@RequiredArgsConstructor
public class ProjectIntentionLocationDTO
{
    @JsonProperty("location")
    private LocationDTO locationDTO;

    @JsonProperty("intention")
    private ProjectIntentionDTO projectIntentionDTO;

    private String chrFeatureTypeName;

    private Integer index;
}
