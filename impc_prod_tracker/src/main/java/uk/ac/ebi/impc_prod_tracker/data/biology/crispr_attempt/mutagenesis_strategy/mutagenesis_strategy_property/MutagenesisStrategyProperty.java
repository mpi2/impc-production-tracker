package uk.ac.ebi.impc_prod_tracker.data.biology.crispr_attempt.mutagenesis_strategy.mutagenesis_strategy_property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

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
