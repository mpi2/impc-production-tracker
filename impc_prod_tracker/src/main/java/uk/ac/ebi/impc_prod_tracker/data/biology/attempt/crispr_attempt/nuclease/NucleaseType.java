package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.nuclease;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class NucleaseType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "nucleaseTypeSeq", sequenceName = "NUCLEASE_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nucleaseTypeSeq")
    private Long id;

    private String name;

    @ManyToOne
    private NucleaseClass nucleaseClass;
}
