package org.gentar.biology.outcome;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.gentar.biology.specimen.Specimen;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.security.abac.Resource;
import org.gentar.security.abac.ResourcePrivacy;
import javax.persistence.*;
import java.util.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Outcome extends BaseEntity implements Resource<Outcome>
{
    @Id
    @SequenceGenerator(name = "outcomeSeq", sequenceName = "OUTCOME_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outcomeSeq")
    private Long id;

    private String tpo;

    @ManyToOne
    private OutcomeType outcomeType;

    @IgnoreForAuditingChanges
    @ManyToOne
    private Plan plan;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "outcomes")
    private Set<Mutation> mutations;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "outcome")
    private Colony colony;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade=CascadeType.ALL, mappedBy = "outcome")
    private Specimen specimen;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @IgnoreForAuditingChanges
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "outcome")
    private Set<PlanStartingPoint> planStartingPoints;

    // Copy Constructor
    public Outcome(Outcome outcome)
    {
        this.id = outcome.id;
        this.tpo = outcome.tpo;
        this.outcomeType = outcome.outcomeType;
        this.plan = outcome.plan;
        if (outcome.mutations != null)
        {
            this.mutations = new HashSet<>(outcome.mutations);
        }
        this.colony = outcome.colony;
        this.specimen = outcome.specimen;
        if (outcome.planStartingPoints != null)
        {
            this.planStartingPoints = new HashSet<>(outcome.planStartingPoints);
        }
    }

    @Override
    public ResourcePrivacy getResourcePrivacy()
    {
        return plan.getProject().getResourcePrivacy();
    }

    @Override
    @JsonIgnore
    public Resource<Outcome> getRestrictedObject()
    {
        // TODO: define the data that we need to show if the user is going to see a restricted
        // version of the object. "this" would shoe the object as it is.
        return this;
    }

    @Override
    public List<WorkUnit> getRelatedWorkUnits()
    {
        return Collections.emptyList();
    }

    @Override
    public List<Consortium> getRelatedConsortia()
    {
        return Collections.emptyList();
    }

    /**
     * Utility method to keep synchronised both parts of the relation outcome-mutation when
     * adding a mutation.
     * @param mutation
     */
    public void addMutation(Mutation mutation)
    {
        if (mutations == null)
        {
            mutations = new HashSet<>();
        }
        this.mutations.add(mutation);
        if (mutation.getOutcomes() == null)
        {
            mutation.setOutcomes(new HashSet<>());
        }
        mutation.getOutcomes().add(this);
    }

    /**
     * Utility method to keep synchronised both parts of the relation outcome-mutation when
     * adding a mutation.
     * @param mutation
     */
    public void deleteMutation(Mutation mutation)
    {
        if (mutations != null)
        {
            mutations.remove(mutation);
        }
        if (mutation.getOutcomes() != null)
        {
            mutation.getOutcomes().remove(this);
        }
    }
}
