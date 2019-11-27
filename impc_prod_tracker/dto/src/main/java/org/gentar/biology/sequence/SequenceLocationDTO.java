package org.gentar.biology.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.location.LocationDTO;

@Data
@RequiredArgsConstructor
public class SequenceLocationDTO {

    private Integer locationIndex;

    @JsonProperty("location")
    private LocationDTO locationDTO;
}
