package org.gentar.biology.targ_rep.targeting_vector.es_cell_distribution_product;

import org.gentar.biology.targ_rep.targeting_vector.TargRepTargetingVectorDistributionProductDTO;
import org.gentar.biology.targ_rep.distribution.targeting_vector_distribution_product.TargRepTargetingVectorDistributionProduct;

import java.util.List;


public class TargRepTargetingVectorDistributionProductMapper {


    public static List<TargRepTargetingVectorDistributionProductDTO> mapToDto(List<TargRepTargetingVectorDistributionProduct> targRepTargetingVectorDistributionProducts) {
        return targRepTargetingVectorDistributionProducts.stream().map(TargRepTargetingVectorDistributionProductMapper::setTargRepTargetingVectorDistributionProductDto).toList();
    }

    private static TargRepTargetingVectorDistributionProductDTO setTargRepTargetingVectorDistributionProductDto(
            TargRepTargetingVectorDistributionProduct targRepTargetingVectorDistributionProduct) {
        TargRepTargetingVectorDistributionProductDTO targRepTargetingVectorDistributionProductDTO = new TargRepTargetingVectorDistributionProductDTO();
        targRepTargetingVectorDistributionProductDTO.setDistributionNetworkName(targRepTargetingVectorDistributionProduct.getDistributionNetwork().getName());
        targRepTargetingVectorDistributionProductDTO.setStartDate(targRepTargetingVectorDistributionProduct.getStartDate());
        targRepTargetingVectorDistributionProductDTO.setEndDate(targRepTargetingVectorDistributionProduct.getEndDate());
        targRepTargetingVectorDistributionProductDTO.setDistributionIdentifier(targRepTargetingVectorDistributionProduct.getDistributionIdentifier());
        return targRepTargetingVectorDistributionProductDTO;
    }


}
