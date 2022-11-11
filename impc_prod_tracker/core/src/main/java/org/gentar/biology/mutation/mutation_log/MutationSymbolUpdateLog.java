package org.gentar.biology.mutation.mutation_log;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;


@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class MutationSymbolUpdateLog extends BaseEntity {

    @Id
    @SequenceGenerator(name = "mutationSymbolUpdateLogSeq", sequenceName = "MUTATION_SYMBOL_UPDATE_LOG_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutationSymbolUpdateLogSeq")
    private Long id;
    private String new_symbol;
    private String old_symbol;
}
