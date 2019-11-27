package org.gentar.biology.molecular_mutation_type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class MolecularMutationType extends BaseEntity {

    @Id
    @SequenceGenerator(name = "molecularMutationTypeSeq", sequenceName = "MOLECULAR_MUTATION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "molecularMutationTypeSeq")
    private Long id;

    private String name;
}
