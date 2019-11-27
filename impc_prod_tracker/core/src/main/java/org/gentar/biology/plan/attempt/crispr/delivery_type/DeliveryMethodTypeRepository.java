package org.gentar.biology.plan.attempt.crispr.delivery_type;

import org.springframework.data.repository.CrudRepository;

public interface DeliveryMethodTypeRepository extends CrudRepository<DeliveryMethodType, Long>
{
    DeliveryMethodType findByName(String deliveryTypeName);
}
