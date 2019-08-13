package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.mutagenesis_donor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.mutagenesis_donor.preparation_type.PreparationType;
import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class MutagenesisDonor extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "mutagenesisDonorSeq", sequenceName = "MUTAGENESIS_DONOR_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutagenesisDonorSeq")
    private Long id;

    @ManyToOne(targetEntity = CrisprAttempt.class)
    private CrisprAttempt crisprAttempt;

    private Double concentration;

    @ManyToOne
    private PreparationType preparationType;

    @Column(columnDefinition = "TEXT")
    private String oligoSequenceFasta;
}
