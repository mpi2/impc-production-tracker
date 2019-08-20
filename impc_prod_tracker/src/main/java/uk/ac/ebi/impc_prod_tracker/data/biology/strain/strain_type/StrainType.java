package uk.ac.ebi.impc_prod_tracker.data.biology.strain.strain_type;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.strain.Strain;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class StrainType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "strainTypeSeq", sequenceName = "STRAIN_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "strainTypeSeq")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "strainTypes")
    private Set<Strain> strains;
}
