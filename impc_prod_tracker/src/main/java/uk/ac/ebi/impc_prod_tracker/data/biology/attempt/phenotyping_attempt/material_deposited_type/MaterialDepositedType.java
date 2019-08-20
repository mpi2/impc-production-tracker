package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.material_deposited_type;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class MaterialDepositedType extends BaseEntity
{

    @Id
    @SequenceGenerator(name = "materialDepositedTypeSeq", sequenceName = "MATERIAL_DEPOSITED_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materialDepositedTypeSeq")
    private Long id;

    private String name;

}
