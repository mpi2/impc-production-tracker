package uk.ac.ebi.impc_prod_tracker.web.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProjectLocationDTO
{
    @JsonProperty("location")
    private LocationDTO locationDTO;

    private String alleleTypeName;
    private String molecularMutationTypeName;
    private String chrFeatureTypeName;
}
