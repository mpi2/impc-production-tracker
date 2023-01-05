package org.gentar.biology.colony.distribution.distribution_network;

import lombok.*;
import org.gentar.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class DistributionNetwork extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "distributionNetworkSeq", sequenceName = "DISTRIBUTION_NETWORK_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "distributionNetworkSeq")
    private Long id;

    private String name;
}
