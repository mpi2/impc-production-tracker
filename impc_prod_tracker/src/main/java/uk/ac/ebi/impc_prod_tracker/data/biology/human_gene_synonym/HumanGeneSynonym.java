package uk.ac.ebi.impc_prod_tracker.data.biology.human_gene_synonym;

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
public class HumanGeneSynonym extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "humanGeneSynonymSeq", sequenceName = "HUMAN_GENE_SYNONYM_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humanGeneSynonymSeq")
    private Long id;

    private String synonym;

    private String name;

    private String omimGeneId;

    @ManyToMany(mappedBy = "humanGeneSynonyms")
    private Set<HumanGene> humanGenes;
}
