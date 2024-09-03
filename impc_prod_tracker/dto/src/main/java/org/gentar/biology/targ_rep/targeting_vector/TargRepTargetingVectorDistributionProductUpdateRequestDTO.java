package org.gentar.biology.targ_rep.targeting_vector;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Getter
@Setter
public class TargRepTargetingVectorDistributionProductUpdateRequestDTO extends RepresentationModel<TargRepTargetingVectorDistributionProductUpdateRequestDTO> {
    private String targetingVectorName;
    private String distributionNetworkName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String distributionIdentifier;
}
