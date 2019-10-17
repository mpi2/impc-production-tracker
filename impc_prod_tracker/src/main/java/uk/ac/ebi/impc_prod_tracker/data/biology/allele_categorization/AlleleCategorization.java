package uk.ac.ebi.impc_prod_tracker.data.biology.allele_categorization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

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

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "alleleCategorizations")
    private Set<ProjectIntention> projectIntentions;
}
