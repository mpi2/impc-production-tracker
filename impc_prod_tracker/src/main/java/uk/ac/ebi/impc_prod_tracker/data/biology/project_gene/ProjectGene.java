package uk.ac.ebi.impc_prod_tracker.data.biology.project_gene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class ProjectGene {
    @Id
    @SequenceGenerator(name = "projectLocationSeq", sequenceName = "PROJECT_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectLocationSeq")
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne(targetEntity = Gene.class)
    private Gene gene;

    @ManyToOne(targetEntity = AlleleType.class)
    private AlleleType alleleType;
}
