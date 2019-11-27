package org.gentar.biology.strain.strain_type;

import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.strain.Strain;
import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class StrainType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "strainTypeSeq", sequenceName = "STRAIN_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "strainTypeSeq")
    private Long id;

    private String name;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "strainTypes")
    private Set<Strain> strains;
}
