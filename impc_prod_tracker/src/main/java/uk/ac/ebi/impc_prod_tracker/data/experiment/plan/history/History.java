package uk.ac.ebi.impc_prod_tracker.data.experiment.plan.history;

import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;

/**
 * Entity to keep the track of the changes executed on a plan.
 */

@Data
@Entity
public class History
{
    @Id
    @SequenceGenerator(name = "historySeq", sequenceName = "HISTORY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historySeq")
    private Long id;

    @ManyToOne
    private Plan plan;

    @Column(name = "user_")
    private String user;

    private LocalDateTime date;

    @Column(columnDefinition = "TEXT")
    private String action;
}
