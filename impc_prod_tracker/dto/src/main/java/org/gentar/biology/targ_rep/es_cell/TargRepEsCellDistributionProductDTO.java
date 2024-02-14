package org.gentar.biology.targ_rep.es_cell;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


/**
 * TargRepDistributionQcDTO.
 */

@Getter
@Setter
public class TargRepEsCellDistributionProductDTO {

    private String distributionNetworkName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String distributionIdentifier;
}
