package uk.ac.ebi.impc_prod_tracker.data.biology.allele.qc_results;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele.Allele;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class AlleleQcResult extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "alleleQcResultSeq", sequenceName = "ALLELE_QC_RESULT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alleleQcResultSeq")
    private Long id;

    @ManyToOne(targetEntity = Allele.class)
    private Allele allele;

    @ManyToOne(targetEntity = QcType.class)
    private QcType qcType;

    @ManyToOne(targetEntity = QcStatus.class)
    private QcStatus status;

}