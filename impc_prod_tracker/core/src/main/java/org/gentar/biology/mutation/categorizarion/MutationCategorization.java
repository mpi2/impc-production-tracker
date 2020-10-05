package org.gentar.biology.mutation.categorizarion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.mutation.categorizarion.type.MutationCategorizationType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
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

    @ManyToOne(targetEntity= MutationCategorizationType.class)
    private MutationCategorizationType mutationCategorizationType;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @IgnoreForAuditingChanges
    @ManyToMany(mappedBy = "mutationCategorizations")
    private Set<ProjectIntention> projectIntentions;

    public String toString()
    {
        String mutationCategorizationTypeName =
            mutationCategorizationType == null ? "Not defined" : mutationCategorizationType.getName();
        return "id:" +id + ", name:" + name + ", description: "+ description +
            ", mutationCategorizationTypeName:" +mutationCategorizationTypeName;
    }
}
