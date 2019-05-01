package uk.ac.ebi.impc_prod_tracker.data.experiment.assignment_status;

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

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Getter
@Setter
@Entity
public class AssignmentStatus extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "assignmentStatusSeq", sequenceName = "ASSIGNMENT_STATUS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assignmentStatusSeq")
    private Long id;

    private String name;
}
