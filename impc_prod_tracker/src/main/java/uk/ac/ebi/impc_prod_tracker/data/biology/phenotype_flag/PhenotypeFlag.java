package uk.ac.ebi.impc_prod_tracker.data.biology.phenotype_flag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotype_attempt.PhenotypeAttempt;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class PhenotypeFlag extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "phenotypeFlagSeq", sequenceName = "PHENOTYPE_FLAG_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phenotypeFlagSeq")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "phenotypeFlags")
    private Set<PhenotypeAttempt> phenotypeAttempts;
}
