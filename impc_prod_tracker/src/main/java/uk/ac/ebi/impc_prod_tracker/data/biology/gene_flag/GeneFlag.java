package uk.ac.ebi.impc_prod_tracker.data.biology.gene_flag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene.HumanGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene.MouseGene;
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
public class GeneFlag extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "geneFlagSeq", sequenceName = "GENE_FLAG_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geneFlagSeq")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "mouseGeneFlags")
    private Set<MouseGene> mouseGenes;

    @ManyToMany(mappedBy = "humanGeneFlags")
    private Set<HumanGene> humanGenes;
}
