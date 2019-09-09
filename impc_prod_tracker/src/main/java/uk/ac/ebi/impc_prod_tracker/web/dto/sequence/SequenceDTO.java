package uk.ac.ebi.impc_prod_tracker.web.dto.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class SequenceDTO {

    private String sequence;

    @JsonProperty("sequence_type_name")
    private String sequenceTypeName;

    @JsonProperty("sequence_location_attributes")
    private List<SequenceLocationDTO> sequenceLocationDTOS;
}
