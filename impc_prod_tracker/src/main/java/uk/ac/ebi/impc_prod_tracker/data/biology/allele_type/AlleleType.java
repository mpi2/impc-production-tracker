package uk.ac.ebi.impc_prod_tracker.data.biology.allele_type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class AlleleType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "alleleTypeSeq", sequenceName = "ALLELE_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alleleTypeSeq")
    private Long id;

    @NotNull
    private String name;
}
