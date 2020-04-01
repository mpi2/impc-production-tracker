package org.gentar.biology.plan.attempt.phenotyping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.outcome.OutcomeDTO;

@Data
@RequiredArgsConstructor
public class PhenotypingAttemptDTO
{
    private Long id;

    private String pin;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long imitsPhenotypeAttemptId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long imitsPhenotypingProductionId;

    private String phenotypingExternalRef;

    @JsonProperty("phenotypingBackgroundStrainName")
    private String strainName;

    @JsonProperty("outcomeDetails")
    private OutcomeDTO outcomeDTO;
}
