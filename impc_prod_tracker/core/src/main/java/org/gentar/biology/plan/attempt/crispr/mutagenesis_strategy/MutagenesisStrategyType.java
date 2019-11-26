package org.gentar.biology.plan.attempt.crispr.mutagenesis_strategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class MutagenesisStrategyType extends BaseEntity {

    @Id
    @SequenceGenerator(name = "mutagenesisStrategyTypeSeq", sequenceName = "MUTAGENESIS_STRATEGY_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutagenesisStrategyTypeSeq")
    private Long id;

    private String name;
}
