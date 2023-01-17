package org.gentar.biology.plan.attempt.breeding;

import lombok.*;
import org.gentar.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

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
