package org.gentar.biology.phenotyping_attempt.material_deposited_type;

import lombok.*;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class MaterialDepositedType extends BaseEntity
{

    @Id
    @SequenceGenerator(name = "materialDepositedTypeSeq", sequenceName = "MATERIAL_DEPOSITED_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materialDepositedTypeSeq")
    private Long id;

    private String name;

}
