package org.gentar.biology.colony;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class DistributionProductDTO
{
    private Long id;
    private String distributionCentreName;
    private String productTypeName;
    private String distributionNetworkName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
