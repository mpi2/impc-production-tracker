package org.gentar.biology.plan.attempt;

import lombok.*;
import org.gentar.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class AttemptType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "attemptTypeSeq", sequenceName = "ATTEMPT_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attemptTypeSeq")
    private Long id;

    private String name;
}
