package org.gentar.biology.colony.distribution.product_type;

import org.springframework.data.repository.CrudRepository;

public interface ProductTypeRepository extends CrudRepository<ProductType, Long>
{
    ProductType findByNameIgnoreCase(String name);
}
