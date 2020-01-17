package org.gentar.biology.mutation.categorizarion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.project.intention.project_intention.ProjectIntention;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class MutationCategorization extends BaseEntity
{

    @Id
    @SequenceGenerator(name = "mutationCategorizationSeq", sequenceName = "MUTATION_CATEGORIZATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mutationCategorizationSeq")
    private Long id;

    @NotNull
    private String name;

    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "mutationCategorizations")
    private Set<ProjectIntention> projectIntentions;
}
