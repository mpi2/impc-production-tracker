package uk.ac.ebi.impc_prod_tracker.data.biology.human_allele_synonym;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_allele.HumanAllele;
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
public class HumanAlleleSynonym extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "humanAlleleSynonymSeq", sequenceName = "HUMAN_ALLELE_SYNONYM_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humanAlleleSynonymSeq")
    private Long id;

    private String synonym;

    private String name;

    @ManyToMany(mappedBy = "humanAlleleSynonyms")
    private Set<HumanAllele> humanAlleles;
}
