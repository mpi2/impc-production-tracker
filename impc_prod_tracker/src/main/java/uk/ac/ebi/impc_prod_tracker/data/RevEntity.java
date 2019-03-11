package uk.ac.ebi.impc_prod_tracker.data;

import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
