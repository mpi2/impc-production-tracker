package org.gentar;

import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Data
@Entity
@RevisionEntity
@Table(name="revision_info")
@EntityListeners(AuditingEntityListener.class)
public class RevEntity
{
    @Id
    @SequenceGenerator(name = "revisionSeq", sequenceName = "REVISION_INFO_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revisionSeq")
    @RevisionNumber
    @Column(name="revision_id")
    private int id;

    @RevisionTimestamp
    @Column(name="revision_timestamp")
    private long timestamp;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;
}
