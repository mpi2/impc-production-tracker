package org.gentar.biology.crispr_attempt.nuclease.nuclease_type;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.crispr_attempt.nuclease.nuclease_class.NucleaseClass;

import javax.persistence.*;

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

    @ToString.Exclude
    @ManyToOne(targetEntity = NucleaseClass.class)
    private NucleaseClass nucleaseClass;
}
