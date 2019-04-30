package uk.ac.ebi.impc_prod_tracker.data.biology.phenotyping_production;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.experiment.colony.Colony;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class PhenotypingProduction extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private Plan plan;

    @ManyToOne
    private Colony colony;

    private LocalDateTime phenotypingExperimentsStarted;

    private Boolean phenotypingStarted;

    private Boolean phenotypingCompleted;

    private Boolean rederivationStarted;

    private Boolean rederivationCompleted;

    private Boolean doNotCountTowardsCompleteness;

}
