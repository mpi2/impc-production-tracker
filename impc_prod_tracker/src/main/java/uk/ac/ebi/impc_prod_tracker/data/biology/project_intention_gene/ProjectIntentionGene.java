package uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_gene;

import lombok.*;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.project_intention.ProjectIntention;
import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class ProjectIntentionGene extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "project_intention_id")
    @MapsId
    private ProjectIntention projectIntention;

    @ManyToOne(targetEntity = Gene.class)
    private Gene gene;
}
