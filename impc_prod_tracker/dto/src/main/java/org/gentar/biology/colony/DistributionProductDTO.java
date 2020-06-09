package org.gentar.biology.colony;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class DistributionProductDTO
{
    @JsonIgnore
    private Long id;
    private String distributionCentreName;
    private String productTypeName;
    private String distributionNetworkName;
    private LocalDateTime endDate;
    private LocalDateTime startDate;
}
