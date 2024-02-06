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
package org.gentar.organization.work_group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.work_unit.WorkUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
//@EqualsAndHashCode( exclude = {"workUnits"}, callSuper = false)
public class WorkGroup extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "workGroupSeq", sequenceName = "WORK_GROUP_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workGroupSeq")
    private Long id;

    private String name;

    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @IgnoreForAuditingChanges
    @ManyToMany(mappedBy = "workGroups")
    private Set<Funder> funders;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @IgnoreForAuditingChanges
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "work_group_work_unit",
            joinColumns = @JoinColumn(name = "work_group_id"),
            inverseJoinColumns = @JoinColumn(name = "work_unit_id"))
    private Set<WorkUnit> workUnits;

}
