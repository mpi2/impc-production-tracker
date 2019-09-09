package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.delivery_type;

import org.springframework.data.repository.CrudRepository;

public interface DeliveryMethodTypeRepository extends CrudRepository<DeliveryMethodType, Long>
{
    DeliveryMethodType findByName(String deliveryTypeName);
}
