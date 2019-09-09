package uk.ac.ebi.impc_prod_tracker.web.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProjectLocationDTO
{
    @JsonProperty("location_attributes")
    private LocationDTO locationDTO;

    @JsonProperty("allele_type_name")
    private String alleleTypeName;

    @JsonProperty("molecular_mutation_type_name")
    private String molecularMutationTypeName;

    @JsonProperty("chr_feature_type_name")
    private String chrFeatureTypeName;
}
