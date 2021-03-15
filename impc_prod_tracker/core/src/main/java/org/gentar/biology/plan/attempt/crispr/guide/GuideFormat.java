package org.gentar.biology.plan.attempt.crispr.guide;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class GuideFormat extends BaseEntity {

    @Id
    @SequenceGenerator(name = "guideFormatSeq", sequenceName = "GUIDE_FORMAT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guideFormatSeq")
    private Long id;

    private String name;
}
