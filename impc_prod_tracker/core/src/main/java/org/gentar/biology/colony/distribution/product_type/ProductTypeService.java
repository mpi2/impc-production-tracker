package org.gentar.biology.colony.distribution.product_type;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeService
{
    private final ProductTypeRepository productTypeRepository;

    public ProductTypeService(ProductTypeRepository productTypeRepository)
    {
        this.productTypeRepository = productTypeRepository;
    }

    public ProductType getProductTypeByName(String name)
    {
        return productTypeRepository.findByNameIgnoreCase(name);
    }

    public ProductType getProductTypeByNameFailIfNull(String name)
    {
        ProductType productType = getProductTypeByName(name);
        if (productType == null)
        {
            throw new UserOperationFailedException("Product Type " + name + " does not exist");
        }
        return productType;
    }
}
