package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_strategy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_strategy.mutagenesis_strategy_property.MutagenesisStrategyProperty;
import uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_strategy.mutagenesis_strategy_type.MutagenesisStrategyType;

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
