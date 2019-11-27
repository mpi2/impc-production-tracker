package org.gentar.biology.plan.attempt.crispr.mutagenesis_strategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class MutagenesisStrategyProperty extends BaseEntity {

    @Id
    @SequenceGenerator(name = "mutagenesisStrategyPropertySeq", sequenceName = "MUTAGENESIS_STRATEGY_PROPERTY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutagenesisStrategyPropertySeq")
    private Long id;

    private String name;
}
