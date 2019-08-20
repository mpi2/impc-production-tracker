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
package uk.ac.ebi.impc_prod_tracker.data.biology.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.conf.security.Resource;
import uk.ac.ebi.impc_prod_tracker.conf.security.ResourcePrivacy;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.Attempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.flag.PlanFlag;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.protocol.Protocol;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.type.PlanType;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.status.Status;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.funder.Funder;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Plan extends BaseEntity implements Resource<Plan>
{
    @Id
    @SequenceGenerator(name = "planSeq", sequenceName = "PLAN_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planSeq")
    private Long id;

    @NotNull
    private String pin;

    @ToString.Exclude
    @ManyToOne
    private Project project;

    @ManyToOne(targetEntity = Funder.class)
    private Funder funder;

    @ManyToOne(targetEntity = Consortium.class)
    private Consortium consortium;

    @ManyToOne(targetEntity = WorkGroup.class)
    private WorkGroup workGroup;

    @ManyToOne(targetEntity = WorkUnit.class)
    private WorkUnit workUnit;

    @NotNull
    private Boolean isActive;

    @NotNull
    @ManyToOne(targetEntity= Status.class)
    private Status status;

    @ManyToOne(targetEntity= PlanType.class)
    private PlanType planType;

//    @NotNull
    @ManyToOne(targetEntity= Privacy.class)
    private Privacy privacy;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Boolean productsAvailableForGeneralPublic;

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

    // Copy Constructor
    public Plan(Plan plan)
    {
        this.id = plan.id;
        this.pin = plan.pin;
        this.isActive = plan.isActive;
        this.project = plan.project;
        this.planType = plan.planType;
        this.privacy = plan.privacy;
        this.workUnit = plan.workUnit;
        this.workGroup = plan.workGroup;
        this.consortium = plan.consortium;
        this.funder = plan.funder;
        this.comment = plan.comment;
        this.attempt = plan.attempt;
        this.planFlags = new HashSet<>(plan.planFlags);
        this.protocols = new HashSet<>(plan.protocols);
        this.status = plan.status;
    }

    @OneToOne(mappedBy = "plan")
    private Attempt attempt;

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
            case "restricted":
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

    @Override
    public String toString()
    {
        return "pin=" + pin + ", type: "+ (this.planType == null ? "Not defined" : planType.getName())
            + ", privacy: " + (privacy == null ? "Not defined" : privacy.getName());
    }
}
