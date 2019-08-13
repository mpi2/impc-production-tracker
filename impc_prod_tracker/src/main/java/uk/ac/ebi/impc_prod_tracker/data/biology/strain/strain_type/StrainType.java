package uk.ac.ebi.impc_prod_tracker.data.biology.strain.strain_type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.strain.Strain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class StrainType extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "materialTypeSeq", sequenceName = "MATERIAL_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materialTypeSeq")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "strainTypes")
    private Set<Strain> strains;
}
