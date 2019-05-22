package uk.ac.ebi.impc_prod_tracker.data.biology.tracked_mouse_allele;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_subtype.AlleleSubtype;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_location.TrackedLocation;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_mouse_gene.TrackedMouseGene;
import javax.persistence.Column;
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
public class TrackedMouseAllele extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "trackedMouseAlleleSeq", sequenceName = "TRACKED_MOUSE_ALLELE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trackedMouseAlleleSeq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name;

    private String alleleSymbol;

    private String mgiId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String autoDescription;

    private Long imitsAlleleId;

    @ManyToOne
    private AlleleType alleleType;

    @ManyToOne
    private AlleleSubtype alleleSubtype;

    @ManyToMany
    @JoinTable(
        name = "tracked_allele_location",
        joinColumns = @JoinColumn(name = "tracked_allele_id"),
        inverseJoinColumns = @JoinColumn(name = "tracked_location_id"))
    private Set<TrackedLocation> trackedLocations;

    @ManyToMany(mappedBy = "trackedMouseAlleles")
    private Set<TrackedMouseGene> trackedMouseGenes;
}
