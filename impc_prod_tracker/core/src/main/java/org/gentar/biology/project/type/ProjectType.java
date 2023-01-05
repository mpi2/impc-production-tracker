package org.gentar.biology.project.type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "projectTypeSeq", sequenceName = "PROJECT_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectTypeSeq")
    private Long id;

    @NotNull
    private String name;
}
