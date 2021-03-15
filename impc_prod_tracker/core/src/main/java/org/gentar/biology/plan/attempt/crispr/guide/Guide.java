package org.gentar.biology.plan.attempt.crispr.guide;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.assay.AssayType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


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

    @NotNull
    @EqualsAndHashCode.Exclude
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

    private String ensemblExonId;

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
