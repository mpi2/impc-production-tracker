package uk.ac.ebi.impc_prod_tracker.data.biology.allele_categorization;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class AlleleCategorization extends BaseEntity
{

    @Id
    @SequenceGenerator(name = "alleleCategorizationSeq", sequenceName = "ALLELE_CATEGORIZATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alleleCategorizationSeq")
    private Long id;

    @NotNull
    private String name;

    private String description;

}
