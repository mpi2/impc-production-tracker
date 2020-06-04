package org.gentar.biology.outcome;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.biology.colony.ColonyDTO;
import org.gentar.biology.specimen.SpecimenDTO;

@Data
public class OutcomeCommonDTO
{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("colony")
    private ColonyDTO colonyDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("specimen")
    private SpecimenDTO specimenDTO;
}
