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
package org.gentar.organization.role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import jakarta.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@AllArgsConstructor
@Data
@Entity
//@Audited
public class Role extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "roleSeq", sequenceName = "ROLE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleSeq")
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    public Role(String name)
    {
        this.name = name;
    }
}
