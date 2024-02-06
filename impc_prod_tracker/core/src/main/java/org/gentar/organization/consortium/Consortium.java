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
package org.gentar.organization.consortium;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.audit.diff.IgnoreForAuditingChanges;
import org.gentar.organization.work_unit.WorkUnit;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
@RestResource(rel = "consortia", path = "consortia")
public class Consortium extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "consortiumSeq", sequenceName = "CONSORTIUM_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consortiumSeq")
    private Long id;

    private String name;

    private String description;

    @ToString.Exclude
    @IgnoreForAuditingChanges
    @ManyToMany
    @JoinTable(
        name = "consortium_work_unit",
        joinColumns = @JoinColumn(name = "consortium_id"),
        inverseJoinColumns = @JoinColumn(name = "work_unit_id"))
    private Set<WorkUnit> workUnits;

}
