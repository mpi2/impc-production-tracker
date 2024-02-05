package org.gentar.biology.colony.distribution;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.gentar.biology.colony.distribution.product_type.ProductType;
import org.gentar.organization.work_unit.WorkUnit;
import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
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
    @JoinColumn(name = "colony_id")
    private Colony colony;

    @OneToOne
    private DistributionNetwork distributionNetwork;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "distributionIdentifier")
    private String distributionIdentifier;

    public String toString()
    {
        String distributionCentreName =
            distributionCentre == null ? "Not defined" : distributionCentre.getName();
        String productTypeName = productType == null ? "Not defined" : productType.getName();
        String distributionNetworkName =
            distributionNetwork == null ? "Not defined" : distributionNetwork.getName();
        return "id:" +id + ", distributionCentre:" + distributionCentreName + ", productType: "
            + productTypeName + ", distributionNetworkName:" +distributionNetworkName
            + ", startDate: " + startDate + ", endDate:" + endDate+ ", distributionIdentifier:" + distributionIdentifier;
    }
}
