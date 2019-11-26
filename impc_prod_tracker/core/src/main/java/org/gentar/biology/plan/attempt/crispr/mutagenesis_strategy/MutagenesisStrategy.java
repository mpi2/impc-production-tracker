package org.gentar.biology.plan.attempt.crispr.mutagenesis_strategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class MutagenesisStrategy extends BaseEntity {

    @Id
    @SequenceGenerator(name = "mutagenesisStrategySeq", sequenceName = "MUTAGENESIS_STRATEGY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutagenesisStrategySeq")
    private Long id;

    @ManyToOne(targetEntity = MutagenesisStrategyType.class)
    private MutagenesisStrategyType mutagenesisStrategyType;

    @ManyToOne(targetEntity = MutagenesisStrategyProperty.class)
    private MutagenesisStrategyProperty mutagenesisStrategyProperty;
}
