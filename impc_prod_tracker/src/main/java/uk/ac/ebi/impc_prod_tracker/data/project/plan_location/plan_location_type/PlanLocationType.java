package uk.ac.ebi.impc_prod_tracker.data.project.plan_location.plan_location_type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class PlanLocationType extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "planLocationTypeSeq", sequenceName = "PLAN_LOCATION_TYPE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planLocationTypeSeq")
    private Long id;

    @NotNull
    private String name;
}
