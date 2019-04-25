package uk.ac.ebi.impc_prod_tracker.data.experiment.assigment_status_stamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.ortholog.Ortholog;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assigment_status.AssigmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;

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
@IdClass(AssigmentStatusStamp.class)
public class AssigmentStatusStamp extends BaseEntity implements Serializable
{
    @Id
    @ManyToOne
    @JoinColumn
    private Project project;

    @Id
    @ManyToOne
    @JoinColumn
    private AssigmentStatus assigmentStatus;

    private LocalDateTime date;
}
