package uk.ac.ebi.impc_prod_tracker.data.project.colony.colony_comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.project.colony.Colony;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class ColonyComment extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "colonyCommentSeq", sequenceName = "COLONY_COMMENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "colonyCommentSeq")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Colony colony;

    private String text;
}
