package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.tissue_distribution;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.PhenotypingAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.material_deposited_type.MaterialDepositedType;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class TissueDistribution extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "tissueDistributionSeq", sequenceName = "TISSUE_DISTRIBUTION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tissueDistributionSeq")
    private Long id;

    @ManyToOne(targetEntity = PhenotypingAttempt.class)
    private PhenotypingAttempt phenotypingAttempt;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @NotNull
    @ManyToOne
    private MaterialDepositedType materialDepositedType;

    @NotNull
    @ManyToOne
    private WorkUnit workUnit;
}
