package uk.ac.ebi.impc_prod_tracker.data.biology.material_type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class MaterialType extends BaseEntity {

    @Id
    @SequenceGenerator(name = "materialTypeSeq", sequenceName = "MATERIAL_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materialTypeSeq")
    private Long id;

    private String name;

}
