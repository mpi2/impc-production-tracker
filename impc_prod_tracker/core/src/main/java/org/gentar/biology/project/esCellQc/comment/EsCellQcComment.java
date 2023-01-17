package org.gentar.biology.project.esCellQc.comment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class EsCellQcComment extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "esCellQcCommentSeq", sequenceName = "ES_CELL_QC_COMMENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esCellQcCommentSeq")
    private Long id;

    @NotNull
    private String name;
}
