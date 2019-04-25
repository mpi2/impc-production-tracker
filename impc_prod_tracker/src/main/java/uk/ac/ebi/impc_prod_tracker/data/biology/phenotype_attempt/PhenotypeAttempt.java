package uk.ac.ebi.impc_prod_tracker.data.biology.phenotype_attempt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.phenotype_flag.PhenotypeFlag;
import uk.ac.ebi.impc_prod_tracker.data.experiment.colony.Colony;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class PhenotypeAttempt extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "phenotypeAttemptSeq", sequenceName = "PHENOTYPE_ATTEMPT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phenotypeAttemptSeq")
    private Long id;

    private String externalRef;

    @ManyToOne
    private Plan plan;

    private Integer numberExperimentsStarted;

    private Integer lateAdultNumberExperimentsStarted;

    @ManyToOne
    private Colony colony;

    @ManyToMany
    @JoinTable(
        name = "phenotype_flag_rel",
        joinColumns = @JoinColumn(name = "phenotype_attempt_id"),
        inverseJoinColumns = @JoinColumn(name = "phenotype_flag_id"))
    private Set<PhenotypeFlag> phenotypeFlags;
}
