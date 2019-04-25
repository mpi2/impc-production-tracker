package uk.ac.ebi.impc_prod_tracker.data.experiment.status_stamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.status.Status;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
@IdClass(StatusStamp.class)
public class StatusStamp extends BaseEntity implements Serializable
{
    @Id
    @ManyToOne
    @JoinColumn
    private Plan plan;

    @Id
    @ManyToOne
    @JoinColumn
    private Status status;

    private LocalDateTime date;
}
