package uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.guide;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class Guide extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "guideSeq", sequenceName = "GUIDE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guideSeq")
    private Long id;

    @ManyToOne(targetEntity = CrisprAttempt.class)
    private CrisprAttempt crisprAttempt;

    private String sequence;

    private String chr;

    private Integer start;

    private Integer stop;

    private String strand;

    private String genomeBuild;

    private Boolean truncatedGuide;

    private Double grnaConcentration;

    private String pam3;

    private String pam5;

    private String protospacerSequence;
}
