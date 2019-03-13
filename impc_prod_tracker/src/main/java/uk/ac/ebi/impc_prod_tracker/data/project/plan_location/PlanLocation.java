package uk.ac.ebi.impc_prod_tracker.data.project.plan_location;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.location.Location;
import uk.ac.ebi.impc_prod_tracker.data.project.plan.Plan;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Data
@Entity
@IdClass(PlanLocation.class)
public class PlanLocation extends BaseEntity implements Serializable
{
    @Id
    @ManyToOne
    @JoinColumn
    private Plan plan;

    @Id
    @ManyToOne
    @JoinColumn
    private Location location;

    private int orderNumber;

    private Boolean targetedSequence;
}
