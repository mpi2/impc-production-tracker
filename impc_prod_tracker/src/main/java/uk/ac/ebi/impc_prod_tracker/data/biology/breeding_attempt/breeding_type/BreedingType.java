package uk.ac.ebi.impc_prod_tracker.data.biology.breeding_attempt.breeding_type;

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
public class BreedingType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "breedingTypeSeq", sequenceName = "BREEDING_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "breedingTypeSeq")
    private Long id;

    private String name;
}
