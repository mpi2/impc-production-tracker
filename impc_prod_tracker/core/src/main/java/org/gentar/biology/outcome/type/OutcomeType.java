package org.gentar.biology.outcome.type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import jakarta.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class OutcomeType extends BaseEntity {

    @Id
    @SequenceGenerator(name = "outcomeTypeSeq", sequenceName = "OUTCOME_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outcomeTypeSeq")
    private Long id;

    private String name;

}
