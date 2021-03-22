package org.gentar.biology.plan.attempt.crispr.guide;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;

import javax.persistence.*;


@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Guide extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "guideSeq", sequenceName = "GUIDE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guideSeq")
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(targetEntity = CrisprAttempt.class)
    @JoinColumn(name = "attempt_id")
    private CrisprAttempt crisprAttempt;

    private String sequence;

    private String guideSequence;

    private String pam;

    private String chr;

    private Integer start;

    private Integer stop;

    private String strand;

    private String genomeBuild;

    private Double grnaConcentration;

    @Column(columnDefinition = "boolean default false")
    private Boolean truncatedGuide;

    @Column(columnDefinition = "boolean default false")
    private Boolean reversed;

    @Column(columnDefinition = "boolean default false")
    private Boolean sangerService;

    @ManyToOne(targetEntity = GuideFormat.class)
    private GuideFormat guideFormat;

    @ManyToOne(targetEntity = GuideSource.class)
    private GuideSource guideSource;

    public String toString()
    {
        return "(seq=" + sequence + ", chr=" + chr + ", start=" + start
                + ", stop=" + stop + ", strand=" + strand
                + ", grnaConcentration=" + grnaConcentration + ")";
    }

}
