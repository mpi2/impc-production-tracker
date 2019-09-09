package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.nuclease.nuclease_type;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.nuclease.nuclease_class.NucleaseClass;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class NucleaseType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "nucleaseTypeSeq", sequenceName = "NUCLEASE_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nucleaseTypeSeq")
    private Long id;

    private String name;

    @ManyToOne(targetEntity = NucleaseClass.class)
    private NucleaseClass nucleaseClass;
}
