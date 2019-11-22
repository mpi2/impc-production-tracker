package org.gentar.web.dto.outcome;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.gentar.web.dto.allele.AlleleDTO;
import org.gentar.web.dto.colony.ColonyDTO;
import org.gentar.web.dto.status_stamps.StatusStampsDTO;

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
