package org.gentar.biology.mutation.qc_results;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.Mutation;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class MutationQcResult extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "mutationQcResultSeq", sequenceName = "MUTATION_QC_RESULT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutationQcResultSeq")
    private Long id;

    @ManyToOne(targetEntity = Mutation.class)
    private Mutation mutation;

    @ManyToOne(targetEntity = QcType.class)
    private QcType qcType;

    @ManyToOne(targetEntity = QcStatus.class)
    private QcStatus status;

}