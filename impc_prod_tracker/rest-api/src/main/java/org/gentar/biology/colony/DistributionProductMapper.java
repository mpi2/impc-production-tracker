package org.gentar.biology.colony;

import org.gentar.Mapper;
import org.gentar.biology.colony.distribution.DistributionProduct;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetworkService;
import org.gentar.biology.colony.distribution.product_type.ProductType;
import org.gentar.biology.colony.distribution.product_type.ProductTypeService;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.organization.work_unit.WorkUnitService;
import org.springframework.stereotype.Component;

@Component
public class DistributionProductMapper implements Mapper<DistributionProduct, DistributionProductDTO>
{
    private WorkUnitService workUnitService;
    private DistributionNetworkService distributionNetworkService;
    private ProductTypeService productTypeService;

    public DistributionProductMapper(
        WorkUnitService workUnitService,
        DistributionNetworkService distributionNetworkService,
        ProductTypeService productTypeService)
    {
        this.workUnitService = workUnitService;
        this.distributionNetworkService = distributionNetworkService;
        this.productTypeService = productTypeService;
    }

    @Override
    public DistributionProductDTO toDto(DistributionProduct distributionProduct)
    {
        DistributionProductDTO distributionProductDTO = new DistributionProductDTO();
        distributionProductDTO.setId(distributionProduct.getId());
        if (distributionProduct.getDistributionCentre() != null)
        {
            distributionProductDTO.setDistributionCentreName(
                distributionProduct.getDistributionCentre().getName());
        }
        if (distributionProduct.getDistributionNetwork() != null)
        {
            distributionProductDTO.setDistributionNetworkName(
                distributionProduct.getDistributionNetwork().getName());
        }
        if (distributionProduct.getProductType() != null)
        {
            distributionProductDTO.setProductTypeName(distributionProduct.getProductType().getName());
        }
        distributionProductDTO.setStartDate(distributionProduct.getStartDate());
        distributionProductDTO.setEndDate(distributionProduct.getEndDate());
        return distributionProductDTO;
    }

    @Override
    public DistributionProduct toEntity(DistributionProductDTO distributionProductDTO)
    {
        DistributionProduct distributionProduct = new DistributionProduct();
        distributionProduct.setId(distributionProductDTO.getId());
        setDistributionCentre(distributionProduct, distributionProductDTO);
        setDistributionNetwork(distributionProduct, distributionProductDTO);
        setProductType(distributionProduct, distributionProductDTO);
        distributionProduct.setStartDate(distributionProductDTO.getStartDate());
        distributionProduct.setEndDate(distributionProductDTO.getEndDate());
        return distributionProduct;
    }

    private void setDistributionCentre(
        DistributionProduct distributionProduct, DistributionProductDTO distributionProductDTO)
    {
        String distributionCentreName = distributionProductDTO.getDistributionCentreName();
        WorkUnit distributionCentre =
            workUnitService.getWorkUnitByNameOrThrowException(distributionCentreName);
        distributionProduct.setDistributionCentre(distributionCentre);
    }

    private void setDistributionNetwork(
        DistributionProduct distributionProduct, DistributionProductDTO distributionProductDTO)
    {
        String distributionNetworkName = distributionProductDTO.getDistributionNetworkName();
        DistributionNetwork distributionNetwork =
            distributionNetworkService.getDistributionNetworkByNameFailIfNull(distributionNetworkName);
        distributionProduct.setDistributionNetwork(distributionNetwork);
    }

    private void setProductType(
        DistributionProduct distributionProduct, DistributionProductDTO distributionProductDTO)
    {
        String productTypeName = distributionProductDTO.getProductTypeName();
        ProductType productType = productTypeService.getProductTypeByNameFailIfNull(productTypeName);
        distributionProduct.setProductType(productType);
    }
}
