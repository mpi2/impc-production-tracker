package uk.ac.ebi.impc_prod_tracker.data.biology.human_allele;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_allele_synonym.HumanAlleleSynonym;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_gene.HumanGene;
import uk.ac.ebi.impc_prod_tracker.data.biology.location.Location;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_allele_synonym.MouseAlleleSynonym;
import uk.ac.ebi.impc_prod_tracker.data.biology.mouse_gene.MouseGene;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class HumanAllele extends BaseEntity{
    @Id
    @SequenceGenerator(name = "humanAlleleSeq", sequenceName = "HUMAN_ALLELE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humanAlleleSeq")
    private Long id;

    @NotNull
    private String name;

    private String alleleSymbol;

    private String hgncAlleleId;

    @ManyToOne(targetEntity = AlleleType.class)
    private AlleleType alleleType;

    @ManyToOne(targetEntity = Location.class)
    private Location location;

    @ManyToMany(mappedBy = "humanAlleles")
    private Set<HumanGene> humanGenes;

    @ManyToMany
    @JoinTable(
        name = "human_allele_synonym_rel",
        joinColumns = @JoinColumn(name = "human_allele_id"),
        inverseJoinColumns = @JoinColumn(name = "human_allele_synonym_id"))
    private Set<HumanAlleleSynonym> humanAlleleSynonyms;
}
