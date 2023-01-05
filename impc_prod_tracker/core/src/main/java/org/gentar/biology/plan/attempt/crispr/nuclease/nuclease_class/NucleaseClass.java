package org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_class;

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
public class NucleaseClass extends BaseEntity
{
    @Id
    @ToString.Exclude
    @SequenceGenerator(name = "nucleaseClassSeq", sequenceName = "NUCLEASE_CLASS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nucleaseClassSeq")
    private Long id;

    private String name;
}
