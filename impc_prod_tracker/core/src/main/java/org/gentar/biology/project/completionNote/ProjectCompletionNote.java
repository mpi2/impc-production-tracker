package org.gentar.biology.project.completionNote;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectCompletionNote  extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "projectCompletionNoteSeq", sequenceName = "PROJECT_COMPLETION_NOTE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectCompletionNoteSeq")
    private Long id;

    @NotNull
    private String note;
}
