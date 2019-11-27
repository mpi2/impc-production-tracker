package org.gentar.biology.outcome;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.biology.allele.AlleleDTO;
import org.gentar.biology.colony.ColonyDTO;
import org.gentar.biology.status_stamps.StatusStampsDTO;

import java.util.List;

@Data
@RequiredArgsConstructor
public class OutcomeDTO
{
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long attemptId;
    private String pin;

    private String statusName;

    @JsonProperty("statusDates")
    private List<StatusStampsDTO> statusStampsDTOS;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("allelesAttributes")
    private List<AlleleDTO> alleleDTOS;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("coloniesAttributes")
    private List<ColonyDTO> colonyDTOS;
}
