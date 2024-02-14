package org.gentar.biology.targ_rep.es_cell.es_cell_distribution_product;

import org.gentar.biology.targ_rep.es_cell.TargRepEsCellDistributionProductDTO;
import org.gentar.biology.targ_rep.distribution.es_cell_distribution_product.TargRepEsCellDistributionProduct;

import java.util.List;


public class TargRepEsCellDistributionProductMapper {


    public static List<TargRepEsCellDistributionProductDTO> mapToDto(List<TargRepEsCellDistributionProduct> targRepEsCellDistributionProducts) {
        return targRepEsCellDistributionProducts.stream().map(TargRepEsCellDistributionProductMapper::setTargRepEsCellDistributionProductDto).toList();
    }

    private static TargRepEsCellDistributionProductDTO setTargRepEsCellDistributionProductDto(
            TargRepEsCellDistributionProduct targRepEsCellDistributionProduct) {
        TargRepEsCellDistributionProductDTO targRepEsCellDistributionProductDTO = new TargRepEsCellDistributionProductDTO();
        targRepEsCellDistributionProductDTO.setDistributionNetworkName(targRepEsCellDistributionProduct.getDistributionNetwork().getName());
        targRepEsCellDistributionProductDTO.setStartDate(targRepEsCellDistributionProduct.getStartDate());
        targRepEsCellDistributionProductDTO.setEndDate(targRepEsCellDistributionProduct.getEndDate());
        targRepEsCellDistributionProductDTO.setDistributionIdentifier(targRepEsCellDistributionProduct.getDistributionIdentifier());
        return targRepEsCellDistributionProductDTO;
    }


}
