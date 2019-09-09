package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.nuclease.nuclease_class;

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
public class NucleaseClass extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "nucleaseClassSeq", sequenceName = "NUCLEASE_CLASS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nucleaseClassSeq")
    private Long id;

    private String name;
}
