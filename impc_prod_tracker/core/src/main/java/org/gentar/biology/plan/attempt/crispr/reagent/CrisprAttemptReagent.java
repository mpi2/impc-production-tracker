package org.gentar.biology.plan.attempt.crispr.reagent;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class CrisprAttemptReagent extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "crisprAttemptReagentSeq", sequenceName = "CRISPR_ATTEMPT_REAGENT_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crisprAttemptReagentSeq")
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    @ManyToOne
    @JoinColumn(name = "attempt_id")
    private CrisprAttempt crisprAttempt;

    @NotNull
    @ManyToOne(targetEntity = Reagent.class)
    private Reagent reagent;

    private Integer concentration;
}
