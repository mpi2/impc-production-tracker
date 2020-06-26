package org.gentar.biology.sequence;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Data
@RequiredArgsConstructor
public class SequenceDTO
{
    private Long id;

    private String sequence;

    @JsonProperty("typeName")
    private String sequenceTypeName;

    @JsonProperty("categoryName")
    private String sequenceCategoryName;

    @JsonProperty("sequenceLocations")
    private List<SequenceLocationDTO> sequenceLocationDTOS;
}
