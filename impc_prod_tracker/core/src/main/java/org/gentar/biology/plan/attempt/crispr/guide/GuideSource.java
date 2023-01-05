package org.gentar.biology.plan.attempt.crispr.guide;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import jakarta.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class GuideSource extends BaseEntity {

    @Id
    @SequenceGenerator(name = "guideSourceSeq", sequenceName = "GUIDE_SOURCE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guideSourceSeq")
    private Long id;

    private String name;
}
