package uk.ac.ebi.impc_prod_tracker.data.biology.project_sequence;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_categorization.AlleleCategorization;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.chromosome_feature_type.ChromosomeFeatureType;
import uk.ac.ebi.impc_prod_tracker.data.biology.location.Location;
import uk.ac.ebi.impc_prod_tracker.data.biology.molecular_mutation_type.MolecularMutationType;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.sequence.Sequence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ProjectSequence extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "projectLocationSeq", sequenceName = "PROJECT_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectLocationSeq")
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Sequence sequence;

    @NotNull
    @ManyToOne(targetEntity= MolecularMutationType.class)
    private MolecularMutationType molecularMutationType;

    @ManyToOne(targetEntity= AlleleCategorization.class)
    private AlleleCategorization alleleCategorization;

    @ManyToOne
    private AlleleType alleleType;

    private int index;

    @ManyToOne(targetEntity= ChromosomeFeatureType.class)
    private ChromosomeFeatureType chromosomeFeatureType;
}
