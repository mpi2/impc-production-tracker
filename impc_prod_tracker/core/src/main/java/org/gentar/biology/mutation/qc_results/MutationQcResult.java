package org.gentar.biology.mutation.qc_results;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.mutation.Mutation;
import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class MutationQcResult extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "mutationQcResultSeq", sequenceName = "MUTATION_QC_RESULT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutationQcResultSeq")
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(targetEntity = Mutation.class)
    private Mutation mutation;

    @ManyToOne(targetEntity = QcType.class)
    private QcType qcType;

    @ManyToOne(targetEntity = QcStatus.class)
    private QcStatus status;

    public String toString()
    {
        String qcTypeName = qcType == null ? "Not defined" : qcType.getName();
        String statusName = status == null ? "Not defined" : status.getName();
        return "id:" +id + ", QC type name:" +qcTypeName + ", QC status:" +statusName;
    }

}