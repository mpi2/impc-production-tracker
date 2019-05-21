package uk.ac.ebi.impc_prod_tracker.data.biology.tracked_location;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_subtype.AlleleSubtype;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleType;
import uk.ac.ebi.impc_prod_tracker.data.biology.species.Species;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_mouse_allele.TrackedMouseAllele;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_strain.TrackedStrain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class TrackedLocation extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "trackedLocationSeq", sequenceName = "TRACKED_LOCATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trackedLocationSeq")
    private Long id;

    private String chromosome;

    private Long start;

    private Long stop;

    @Pattern(regexp = "^[\\+-\\?]{1}$", message = "The values allowed for the strand are: '+', '-', or if the value es unknown enter '?'.")
    private String strand;

    private String genomeBuild;

    @ManyToOne
    private TrackedStrain strain;

    @ManyToOne(targetEntity = Species.class)
    private Species species;

    @Column(columnDefinition = "TEXT")
    private String sequence;

    private String mgiId;

    @Column(columnDefinition = "TEXT")
    private String variantName;

    private String variantSymbol;

    @Lob
    @Column(name="bam_file")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] bam_file;

    @Lob
    @Column(name="bam_file_index")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] bam_file_index;

    @Lob
    @Column(name="vcf_file")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] vcf_file;

    @Lob
    @Column(name="vcf_file_index")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] vcf_file_index;


    @ManyToOne
    private AlleleType alleleType;

    @ManyToOne
    private AlleleSubtype alleleSubtype;

    @ManyToMany(mappedBy = "trackedLocations")
    private Set<TrackedMouseAllele> trackedMouseAlleles;
}
