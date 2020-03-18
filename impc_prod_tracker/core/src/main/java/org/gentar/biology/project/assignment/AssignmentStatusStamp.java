package org.gentar.biology.project.assignment;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.project.Project;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
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
