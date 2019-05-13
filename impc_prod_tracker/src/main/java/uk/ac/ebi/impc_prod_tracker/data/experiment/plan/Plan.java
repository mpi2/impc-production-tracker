/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package uk.ac.ebi.impc_prod_tracker.data.experiment.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.conf.security.Resource;
import uk.ac.ebi.impc_prod_tracker.conf.security.ResourcePrivacy;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan_reagent.PlanReagent;
import uk.ac.ebi.impc_prod_tracker.data.experiment.colony.Colony;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.funder.Funder;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.flag.PlanFlag;
import uk.ac.ebi.impc_prod_tracker.data.experiment.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.protocol.Protocol;
import uk.ac.ebi.impc_prod_tracker.data.experiment.status.Status;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.type.PlanType;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class Plan extends BaseEntity implements Resource<Plan>
{
    @Id
    @SequenceGenerator(name = "planSeq", sequenceName = "PLAN_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planSeq")
    private Long id;

    @NotNull
    private String pin;

    @NotNull
    private Boolean isActive;

    @ManyToOne
    private Project project;


    @ManyToOne(targetEntity= PlanType.class)
    private PlanType planType;

    @NotNull
    @ManyToOne(targetEntity= Privacy.class)
    private Privacy privacy;

    @ManyToOne(targetEntity = WorkUnit.class)
    private WorkUnit workUnit;

    @ManyToOne(targetEntity = WorkGroup.class)
    private WorkGroup workGroup;

    @ManyToOne(targetEntity = Consortium.class)
    private Consortium consortium;

    @ManyToOne(targetEntity = Funder.class)
    private Funder funder;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "parent_colony_id")
    private Colony colony;

    @ManyToMany()
    @JoinTable(
        name = "plan_flag_relation",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "plan_flag_id"))
    private Set<PlanFlag> planFlags;

    @ManyToMany
    @JoinTable(
        name = "plan_protocol",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "protocol_id"))
    private Set<Protocol> protocols;

    @NotNull
    @ManyToOne(targetEntity= Status.class)
    private Status status;

    @OneToMany(mappedBy = "plan")
    private Set<PlanReagent> planReagents;

    @Override
    @JsonIgnore
    public ResourcePrivacy getResourcePrivacy()
    {
        ResourcePrivacy resourcePrivacy;
        switch (privacy.getName().toLowerCase())
        {
            case "public":
                resourcePrivacy = ResourcePrivacy.PUBLIC;
                break;
            case "protected":
                resourcePrivacy = ResourcePrivacy.PROTECTED;
                break;
            case "private":
                resourcePrivacy = ResourcePrivacy.RESTRICTED;
                break;
            default:
                throw new OperationFailedException("Invalid privacy");
        }
        return resourcePrivacy;
    }

    @Override
    @JsonIgnore
    public Plan getRestrictedObject()
    {
        Plan plan = new Plan();
        plan.setPrivacy(this.privacy);
        plan.setPin(this.pin);
        plan.setPlanType(this.planType);
        Project restrictedProject = new Project();
        restrictedProject.setTpn(this.project.getTpn());
        plan.setProject(restrictedProject);

        return plan;
    }
}
