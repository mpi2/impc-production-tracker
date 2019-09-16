package uk.ac.ebi.impc_prod_tracker.data.biology.allele.qc_results;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class QcStatus extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "qcStatusSeq", sequenceName = "QC_STATUS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qcStatusSeq")
    private Long id;

    @NotNull
    private String name;

}
