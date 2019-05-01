package uk.ac.ebi.impc_prod_tracker.data.experiment.assignment_status_stamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assignment_status.AssignmentStatus;
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
@IdClass(AssignmentStatusStamp.class)
public class  AssignmentStatusStamp extends BaseEntity implements Serializable
{
    @Id
    @ManyToOne
    @JoinColumn
    private Project project;

    @Id
    @ManyToOne
    @JoinColumn
    private AssignmentStatus assignmentStatus;

    private LocalDateTime date;
}
