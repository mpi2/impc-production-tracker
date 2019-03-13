package uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene_synomym;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
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
public class MouseGeneSynonym extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "mouseGeneSynonymSeq", sequenceName = "MOUSE_GENE_SYNONYM_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mouseGeneSynonymSeq")
    private Long id;

    private String synonym;

    private String name;

    @ManyToMany(mappedBy = "mouseGeneSynonyms")
    private Set<MouseGene> mouseGenes;
}
