package org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_type;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.*;
import org.gentar.BaseEntity;


@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class NucleaseType extends BaseEntity
{
    @Id
    @ToString.Exclude
    @SequenceGenerator(name = "nucleaseTypeSeq", sequenceName = "NUCLEASE_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nucleaseTypeSeq")
    private Long id;

    private String name;

}
