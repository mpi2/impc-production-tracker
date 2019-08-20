package uk.ac.ebi.impc_prod_tracker.data.common.history.detail;

import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class HistoryDetail extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "historyDetailSeq", sequenceName = "HISTORY_DETAIL_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historyDetailSeq")
    private Long id;

    private String field;

    private String oldValue;

    private String newValue;

    private String referenceEntity;

    private String oldValueEntityId;

    private String newValueEntityId;
}
