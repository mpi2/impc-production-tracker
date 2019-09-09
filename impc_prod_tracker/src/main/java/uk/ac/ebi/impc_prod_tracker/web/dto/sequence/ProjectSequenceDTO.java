package uk.ac.ebi.impc_prod_tracker.web.dto.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.LocationDTO;

@Data
@RequiredArgsConstructor
public class ProjectSequenceDTO {

    @JsonProperty("sequence_attributes")
    private SequenceDTO sequenceDTO;

    @JsonProperty("allele_type_name")
    private String alleleTypeName;

    @JsonProperty("molecular_mutation_type_name")
    private String molecularMutationTypeName;

    @JsonProperty("chr_feature_type_name")
    private String chrFeatureTypeName;
}
