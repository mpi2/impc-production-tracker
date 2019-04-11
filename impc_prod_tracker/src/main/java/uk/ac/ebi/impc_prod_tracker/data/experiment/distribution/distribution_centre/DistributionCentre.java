package uk.ac.ebi.impc_prod_tracker.data.experiment.distribution.distribution_centre;

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
public class DistributionCentre extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "distributionCentreSeq", sequenceName = "DISTRIBUTION_CENTRE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "distributionCentreSeq")
    private Long id;

    private String name;
}
