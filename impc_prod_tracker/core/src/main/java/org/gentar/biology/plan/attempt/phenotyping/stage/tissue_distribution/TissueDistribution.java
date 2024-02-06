package org.gentar.biology.plan.attempt.phenotyping.stage.tissue_distribution;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.material_deposited_type.MaterialDepositedType;
import org.gentar.organization.work_unit.WorkUnit;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
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

    public String toString()
    {
        String materialDepositedTypeName =
            materialDepositedType == null ? "Not defined" : materialDepositedType.getName();
        String workUnitName = workUnit == null ? "Not defined" : workUnit.getName();
        return "id:" +id + ", startDate:" + startDate + ", endDate: "+ endDate
            + ", material Deposited Type Name:" + materialDepositedTypeName
            + ", work unit name: " +workUnitName;
    }
}
