package org.gentar.biology.strain.excision_type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ExcisionType extends BaseEntity {
    @Id
    @SequenceGenerator(name = "excisionTypeSeq", sequenceName = "EXCISION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "excisionTypeSeq")
    private Long id;

    @NotNull
    private String name;
}
