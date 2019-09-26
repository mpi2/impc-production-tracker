package uk.ac.ebi.impc_prod_tracker.web.dto.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProjectSequenceDTO {

    @JsonProperty("sequenceAttributes")
    private SequenceDTO sequenceDTO;

    private String alleleTypeName;
    private String molecularMutationTypeName;
    private String chrFeatureTypeName;
}
