package org.gentar.biology.plan.attempt.crispr.nuclease.nuclease_class;

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
public class NucleaseClass extends BaseEntity
{
    @Id
    @ToString.Exclude
    @SequenceGenerator(name = "nucleaseClassSeq", sequenceName = "NUCLEASE_CLASS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nucleaseClassSeq")
    private Long id;

    private String name;
}
