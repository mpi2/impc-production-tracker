package org.gentar.biology.targ_rep.distribution.targeting_vector_distribution_product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.gentar.biology.targ_rep.targeting_vector.TargRepTargetingVector;

import java.time.LocalDate;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepTargetingVectorDistributionProduct extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepTargetingVectorDistributionProductSeq", sequenceName = "TARG_REP_TARGETING_VECTOR_DISTRIBUTION_PRODUCT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepTargetingVectordistributionProductSeq")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "targ_rep_targeting_vector_id")
    private TargRepTargetingVector targRepTargetingVector;

    @ManyToOne
    @JoinColumn(name = "distribution_network_id")
    private DistributionNetwork distributionNetwork;


    private LocalDate startDate;

    private LocalDate endDate;

    private String distributionIdentifier;
}
