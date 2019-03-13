package uk.ac.ebi.impc_prod_tracker.data.biology.mouse_allele_synonym;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_allele.MouseAllele;
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
public class MouseAlleleSynonym extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "mouseAlleleSynonymSeq", sequenceName = "MOUSE_ALLELE_SYNONYM_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mouseAlleleSynonymSeq")
    private Long id;

    private String synonym;

    private String name;

    @ManyToMany(mappedBy = "mouseAlleleSynonyms")
    private Set<MouseAllele> mouseAlleles;
}
