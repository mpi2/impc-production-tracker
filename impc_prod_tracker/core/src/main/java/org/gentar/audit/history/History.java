package org.gentar.audit.history;

import lombok.Data;
import org.gentar.BaseEntity;
import org.gentar.audit.history.detail.HistoryDetail;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity to keep the track of the changes executed on an entity.
 */

@Data
@Entity
public class History extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "historySeq", sequenceName = "HISTORY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historySeq")
    private Long id;

    private String entityName;

    private Long entityId;

    @Column(name = "user_")
    private String user;

    private LocalDateTime date;

    private String comment;

    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name = "history_id", nullable=false)
    private List<HistoryDetail> historyDetailSet = new ArrayList<>();

}
