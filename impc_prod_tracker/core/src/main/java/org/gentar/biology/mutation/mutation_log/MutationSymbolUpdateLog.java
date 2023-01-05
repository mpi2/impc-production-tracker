package org.gentar.biology.mutation.mutation_log;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
