package org.gentar.web.dto.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Data
@RequiredArgsConstructor
public class SequenceDTO {

    private String sequence;

    @JsonProperty("typeName")
    private String sequenceTypeName;

    private String sequenceCategoryName;

    @JsonProperty("sequenceLocations")
    private List<SequenceLocationDTO> sequenceLocationDTOS;
}
