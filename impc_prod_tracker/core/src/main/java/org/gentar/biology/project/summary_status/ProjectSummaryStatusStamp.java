package org.gentar.biology.project.summary_status;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.project.Project;
import org.gentar.biology.status.Status;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectSummaryStatusStamp extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "projectSummaryStatusStampSeq", sequenceName = "PROJECT_SUMMARY_STATUS_STAMP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectSummaryStatusStampSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Project.class)
    private Project project;

    @NotNull
    @ManyToOne(targetEntity = Status.class)
    private Status status;

    @NotNull
    private LocalDateTime date;
}
