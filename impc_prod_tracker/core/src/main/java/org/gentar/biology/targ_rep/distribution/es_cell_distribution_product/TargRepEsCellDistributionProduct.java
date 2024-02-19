package org.gentar.biology.targ_rep.distribution.es_cell_distribution_product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepEsCellDistributionProduct extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepEsCellDistributionProductSeq", sequenceName = "TARG_REP_ES_CELL_DISTRIBUTION_PRODUCT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepEsCellDistributionProductSeq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "targ_rep_es_cell_id")
    private TargRepEsCell targRepEsCell;

    @ManyToOne
    @JoinColumn(name = "distribution_network_id")
    private DistributionNetwork distributionNetwork;


    private LocalDate startDate;

    private LocalDate endDate;

    private String distributionIdentifier;
}
