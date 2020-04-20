package org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedType;
import org.gentar.organization.work_unit.WorkUnit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class TissueDistribution extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "tissueDistributionSeq", sequenceName = "TISSUE_DISTRIBUTION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tissueDistributionSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = PhenotypingStage.class)
    private PhenotypingStage phenotypingStage;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    @ManyToOne(targetEntity = MaterialDepositedType.class)
    private MaterialDepositedType materialDepositedType;

    @NotNull
    @ManyToOne(targetEntity = WorkUnit.class)
    private WorkUnit workUnit;
}
