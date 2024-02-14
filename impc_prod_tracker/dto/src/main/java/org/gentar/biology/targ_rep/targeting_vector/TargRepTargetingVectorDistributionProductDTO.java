package org.gentar.biology.targ_rep.targeting_vector;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class TargRepTargetingVectorDistributionProductDTO {

    private String distributionNetworkName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String distributionIdentifier;
}
