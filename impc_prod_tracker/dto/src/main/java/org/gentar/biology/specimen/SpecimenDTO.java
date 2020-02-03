package org.gentar.biology.specimen;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class SpecimenDTO
{
    private Long id;
    private String specimenExternalRef;
    private String specimenTypeName;

    @JsonProperty("specimenProperties")
    private List<SpecimenPropertyDTO> specimenPropertyDTOS;
}
