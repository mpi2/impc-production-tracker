package org.gentar.biology.colony.distribution;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.gentar.biology.colony.distribution.product_type.ProductType;
import org.gentar.organization.work_unit.WorkUnit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class DistributionProduct extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "distributionProductSeq", sequenceName = "DISTRIBUTION_PRODUCT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "distributionProductSeq")
    private Long id;

    @NotNull
    @OneToOne
    private WorkUnit distributionCentre;

    @NotNull
    @OneToOne
    private ProductType productType;

    @ManyToOne
    private Colony colony;

    @OneToOne
    private DistributionNetwork distributionNetwork;

    private LocalDateTime start_date;

    private LocalDateTime end_date;
}
