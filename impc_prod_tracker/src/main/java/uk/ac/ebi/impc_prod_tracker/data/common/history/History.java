package uk.ac.ebi.impc_prod_tracker.data.common.history;

import lombok.Data;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.common.history.detail.HistoryDetail;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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

    private String action;

    private String comment;

    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name = "history_id", nullable=false)
    private List<HistoryDetail> historyDetailSet = new ArrayList<>();

}
