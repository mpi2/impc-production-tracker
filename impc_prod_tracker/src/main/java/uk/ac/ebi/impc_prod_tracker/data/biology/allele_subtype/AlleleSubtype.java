package uk.ac.ebi.impc_prod_tracker.data.biology.allele_subtype;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class AlleleSubtype extends BaseEntity
{

    @Id
    @SequenceGenerator(name = "alleleSubtypeSeq", sequenceName = "ALLELE_SUBTYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alleleSubtypeSeq")
    private Long id;

    @NotNull
    private String name;

    private String description;

}
