package org.gentar.biology.colony;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class DistributionProductDTO
{
    private Long id;
    private String distributionCentreName;
    private String productTypeName;
    private String distributionNetworkName;
    private LocalDate startDate;
    private LocalDate endDate;
}
