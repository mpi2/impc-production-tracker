/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.organization.funder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.biology.plan.Plan;
import org.gentar.organization.work_group.WorkGroup;

import jakarta.persistence.*;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class Funder extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "funderSeq", sequenceName = "FUNDER_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funderSeq")
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @IgnoreForAuditingChanges
    @JsonIgnore
    @ManyToMany(mappedBy = "funders")
    private Set<Plan> plans;

    @ManyToMany
    @IgnoreForAuditingChanges
    @JoinTable(
        name = "funder_work_group",
        joinColumns = @JoinColumn(name = "funder_id"),
        inverseJoinColumns = @JoinColumn(name = "work_group_id"))
    private Set<WorkGroup> workGroups;
}
