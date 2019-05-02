package uk.ac.ebi.impc_prod_tracker.data.experiment.assignment_status_stamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.human_disease.HumanDisease;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assignment_status.AssignmentStatus;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class  AssignmentStatusStamp extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "assignmentStatusStampSeq", sequenceName = "ASSIGNMENT_STATUS_STAMP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assignmentStatusStampSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Project.class)
    private Project project;

    @NotNull
    @ManyToOne(targetEntity = AssignmentStatus.class)
    private AssignmentStatus assignmentStatus;

    @NotNull
    private LocalDateTime date;
}
