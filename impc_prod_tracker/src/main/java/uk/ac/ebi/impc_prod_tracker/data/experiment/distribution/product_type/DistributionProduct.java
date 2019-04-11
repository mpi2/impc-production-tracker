package uk.ac.ebi.impc_prod_tracker.data.experiment.distribution.product_type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.experiment.colony.Colony;
import uk.ac.ebi.impc_prod_tracker.data.experiment.distribution.distribution_centre.DistributionCentre;
import uk.ac.ebi.impc_prod_tracker.data.experiment.distribution.distribution_network.DistributionNetwork;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

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

    @ManyToOne
    private DistributionCentre distributionCentre;

    @ManyToOne
    private ProductType productType;

    @ManyToOne
    private Colony colony;

    @ManyToOne
    private DistributionNetwork distributionNetwork;
}
