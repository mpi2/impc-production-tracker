package uk.ac.ebi.impc_prod_tracker.data.experiment.project_location;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.IntentedLocation.IntendedLocation;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
@IdClass(ProjectLocation.class)
public class ProjectLocation extends BaseEntity implements Serializable
{
    @Id
    @ManyToOne
    @JoinColumn
    private Project project;

    @Id
    @ManyToOne
    @JoinColumn
    private IntendedLocation location;

    private int index;
}
