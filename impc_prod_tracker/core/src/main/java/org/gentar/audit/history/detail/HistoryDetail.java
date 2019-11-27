package org.gentar.audit.history.detail;

import lombok.Data;
import org.gentar.BaseEntity;

import javax.persistence.Column;
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

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    private String referenceEntity;

    private String oldValueEntityId;

    private String newValueEntityId;

}
