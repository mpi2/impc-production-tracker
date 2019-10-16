package uk.ac.ebi.impc_prod_tracker.data.biology.project_intention;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.molecular_mutation_type.MolecularMutationType;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.type.IntentionType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectIntention
{
    @Id
    @SequenceGenerator(name = "projectIntentionSeq", sequenceName = "PROJECT_INTENTION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectIntentionSeq")
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private MolecularMutationType molecularMutationType;

    @ManyToOne
    private AlleleType alleleType;

    @ManyToOne
    private IntentionType intentionType;
}
