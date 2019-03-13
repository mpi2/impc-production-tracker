package uk.ac.ebi.impc_prod_tracker.data.biology.human_disease;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_disease_synonym.HumanDiseaseSynonym;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene.HumanGene;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class HumanDisease extends BaseEntity {
    @Id
    @SequenceGenerator(name = "humanDiseaseSeq", sequenceName = "HUMAN_DISEASE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humanDiseaseSeq")
    private Long id;

    private String name;

    private String omimDiseaseId;

    private String notes;

    @ManyToMany(mappedBy = "humanDiseases")
    private Set<HumanGene> humanGenes;


    @ManyToMany
    @JoinTable(
        name = "human_disease_synonym_rel",
        joinColumns = @JoinColumn(name = "human_disease_id"),
        inverseJoinColumns = @JoinColumn(name = "human_disease_synonym_id"))
    private Set<HumanDiseaseSynonym> humanDiseaseSynonyms;


}
