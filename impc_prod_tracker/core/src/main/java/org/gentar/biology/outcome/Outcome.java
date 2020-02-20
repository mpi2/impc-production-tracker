package org.gentar.biology.outcome;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.plan.Plan;
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

    // Copy Constructor
    public Outcome(Outcome outcome)
    {
        this.id = outcome.id;
        this.tpo = outcome.tpo;
        this.outcomeType = outcome.outcomeType;
        this.plan = outcome.plan;
        this.mutations = new HashSet<>(outcome.mutations);
        this.colony = outcome.colony;
        this.specimen = outcome.specimen;
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
}
