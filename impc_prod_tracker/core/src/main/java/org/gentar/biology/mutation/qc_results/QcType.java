package org.gentar.biology.mutation.qc_results;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class QcType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "qcTypeSeq", sequenceName = "QC_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qcTypeSeq")
    private Long id;

    @NotNull
    private String name;
}
