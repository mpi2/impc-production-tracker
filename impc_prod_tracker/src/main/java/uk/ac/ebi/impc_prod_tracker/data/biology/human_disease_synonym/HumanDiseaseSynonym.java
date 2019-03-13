package uk.ac.ebi.impc_prod_tracker.data.biology.human_disease_synonym;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_disease.HumanDisease;

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
public class HumanDiseaseSynonym extends BaseEntity {

    @Id
    @SequenceGenerator(name = "humanDiseaseSynonymSeq", sequenceName = "HUMAN_DISEASE_SYNONYM_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humanDiseaseSynonymSeq")
    private Long id;

    private String name;

    private String omimDiseaseId;

    @ManyToMany(mappedBy = "humanDiseaseSynonyms")
    private Set<HumanDisease> humanDiseases;
}
