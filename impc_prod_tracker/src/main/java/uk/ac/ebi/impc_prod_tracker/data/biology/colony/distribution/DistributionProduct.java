package uk.ac.ebi.impc_prod_tracker.data.biology.colony.distribution;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.colony.Colony;
import uk.ac.ebi.impc_prod_tracker.data.biology.colony.distribution.distribution_network.DistributionNetwork;
import uk.ac.ebi.impc_prod_tracker.data.biology.colony.distribution.product_type.ProductType;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
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
