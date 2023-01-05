package org.gentar.biology.intention.project_intention;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.project.Project;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.intention.project_intention_sequence.ProjectIntentionSequence;
import org.gentar.biology.sequence_location.SequenceLocation;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class ProjectIntention
{
    @Id
    @SequenceGenerator(name = "projectIntentionSeq", sequenceName = "PROJECT_INTENTION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projectIntentionSeq")
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private MolecularMutationType molecularMutationType;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade= CascadeType.ALL, mappedBy = "projectIntention")
    private ProjectIntentionGene projectIntentionGene;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "project_intention_id")
    private Set<ProjectIntentionSequence> projectIntentionSequences;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "project_intention_mutation_categorization",
        joinColumns = @JoinColumn(name = "project_intention_id"),
        inverseJoinColumns = @JoinColumn(name = "mutation_categorization_id")
    )
    private Set<MutationCategorization> mutationCategorizations;
}
