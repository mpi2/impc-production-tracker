package org.gentar.biology.plan.attempt.crispr.guide;

import jakarta.persistence.*;
import lombok.*;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class MergedGuide {

    @Id
    @SequenceGenerator(name = "mergedGuideSeq", sequenceName = "MERGED_GUIDE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mergedGuideSeq")
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

    private Boolean truncatedGuide;

    private Boolean reversed;

    private Boolean sangerService;

    @ManyToOne(targetEntity = GuideFormat.class)
    private GuideFormat guideFormat;

    @ManyToOne(targetEntity = GuideSource.class)
    private GuideSource guideSource;

    private String gid;
}
