package org.gentar.audit.history.detail;

import lombok.Data;
import org.gentar.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

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

    private String note;

}
