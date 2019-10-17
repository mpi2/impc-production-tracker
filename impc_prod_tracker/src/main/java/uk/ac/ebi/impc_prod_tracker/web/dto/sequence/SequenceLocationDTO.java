package uk.ac.ebi.impc_prod_tracker.web.dto.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.LocationDTO;

@Data
@RequiredArgsConstructor
public class SequenceLocationDTO {

    private Integer locationIndex;

    @JsonProperty("location")
    private LocationDTO locationDTO;
}
