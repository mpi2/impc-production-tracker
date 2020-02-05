package org.gentar.biology.plan.attempt.crispr.guide;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;


@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Guide extends BaseEntity
{
    @EqualsAndHashCode.Exclude
    @Id
    @SequenceGenerator(name = "guideSeq", sequenceName = "GUIDE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guideSeq")
    private Long id;

    @EqualsAndHashCode.Exclude
    @ManyToOne(targetEntity = CrisprAttempt.class)
    @JoinColumn(name = "attempt_id")
    private CrisprAttempt crisprAttempt;

    private String sequence;

    private String chr;

    private Integer start;

    private Integer stop;

    private String strand;

    private String genomeBuild;

    private Boolean truncatedGuide;

    private String pam3;

    private String pam5;

    private String protospacerSequence;

    public String toString()
    {
        return "(seq=" + sequence + ", chr=" + chr + ", start=" + start
            + ", stop=" + stop + ", truncated?=" + truncatedGuide +")";
    }
}
