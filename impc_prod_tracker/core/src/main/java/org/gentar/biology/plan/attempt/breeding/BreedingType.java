package org.gentar.biology.plan.attempt.breeding;

import lombok.*;
import org.gentar.BaseEntity;
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
