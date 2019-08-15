package uk.ac.ebi.impc_prod_tracker.data.biology.colony.distribution.distribution_network;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class DistributionNetwork extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "distributionNetworkSeq", sequenceName = "DISTRIBUTION_NETWORK_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "distributionNetworkSeq")
    private Long id;

    private String name;
}
