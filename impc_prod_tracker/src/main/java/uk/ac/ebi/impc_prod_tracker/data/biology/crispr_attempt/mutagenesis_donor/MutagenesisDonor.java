package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_donor;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_donor.preparation_type.PreparationType;
import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class MutagenesisDonor extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "mutagenesisDonorSeq", sequenceName = "MUTAGENESIS_DONOR_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutagenesisDonorSeq")
    private Long id;

    @ManyToOne(targetEntity = CrisprAttempt.class)
    @JoinColumn(name = "attempt_id")
    private CrisprAttempt crisprAttempt;

    private Double concentration;

    @ManyToOne
    private PreparationType preparationType;

    @Column(columnDefinition = "TEXT")
    private String oligoSequenceFasta;

    @Override
    public String toString()
    {
        String preparationTypeName = preparationType == null ? "Not defined" : preparationType.getName();
        return "(" + "oligo Sequence Fasta: " + oligoSequenceFasta + ", "
            + "Preparation Type: " + preparationTypeName + ", " + "Concentration: " + concentration + ")";
    }
}
