package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.mutagenesis_donor.preparation_type;

import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class PreparationType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "preparationTypeSeq", sequenceName = "PREPARATION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "preparationTypeSeq")
    private Long id;

    private String name;
}
