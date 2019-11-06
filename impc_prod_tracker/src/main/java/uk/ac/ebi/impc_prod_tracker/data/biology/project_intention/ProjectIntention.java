package uk.ac.ebi.impc_prod_tracker.data.biology.project_intention;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_categorization.AlleleCategorization;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.molecular_mutation_type.MolecularMutationType;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_gene.ProjectIntentionGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.type.IntentionType;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_sequence.ProjectIntentionSequence;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.util.Set;

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

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade= CascadeType.ALL, mappedBy = "projectIntention")
    private ProjectIntentionGene projectIntentionGene;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "projectIntention")
    private ProjectIntentionSequence projectIntentionSequence;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "project_intention_allele_categorization",
        joinColumns = @JoinColumn(name = "project_intention_id"),
        inverseJoinColumns = @JoinColumn(name = "allele_categorization_id"))
    private Set<AlleleCategorization> alleleCategorizations;
}
