package org.gentar.biology.targ_rep.es_cell;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Getter
@Setter
public class TargRepEsCellDistributionProductUpdateRequestDTO extends RepresentationModel<TargRepEsCellDistributionProductUpdateRequestDTO> {
    private String esCellName;
    private String distributionNetworkName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String distributionIdentifier;
}
