package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.delivery_type;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class DeliveryMethodType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "deliveryMethodTypeSeq", sequenceName = "DELIVERY_METHOD_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deliveryMethodTypeSeq")
    private Long id;

    private String name;
}
