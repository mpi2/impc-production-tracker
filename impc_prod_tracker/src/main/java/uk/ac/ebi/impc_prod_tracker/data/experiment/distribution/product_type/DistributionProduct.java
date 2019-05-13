package uk.ac.ebi.impc_prod_tracker.data.experiment.distribution.product_type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.experiment.colony.Colony;
import uk.ac.ebi.impc_prod_tracker.data.experiment.distribution.distribution_network.DistributionNetwork;
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

    private LocalDateTime start_date;

    private LocalDateTime end_date;

    @Column(name="reconciled", columnDefinition="varchar(255) default 'not checked'", nullable = false)
    private String reconciled;

    private LocalDateTime reconciled_at;

    @Column(name="available", columnDefinition="Boolean default false", nullable = false)
    private Boolean available;

    @NotNull
    @ManyToOne
    private WorkUnit distributionCentre;

    @NotNull
    @ManyToOne
    private ProductType productType;

    @ManyToOne
    private Colony colony;

    @ManyToOne
    private DistributionNetwork distributionNetwork;
}
