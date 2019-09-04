package uk.ac.ebi.impc_prod_tracker.web.dto.colony.DistributionCentresDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class DistributionCentreDTO
{
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long colonyId;
    private String distributionNetworkName;
    private LocalDateTime endDate;
    private LocalDateTime startDate;
    private String materialDepositedName;
    private String workUnitName;
    private Boolean isDistributedByEmma;
}
