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
package org.gentar.organization.institute;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.gentar.BaseEntity;
import org.gentar.biology.project.consortium.ProjectConsortium;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@EqualsAndHashCode( callSuper = false)
@Entity
public class Institute extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "instituteSeq", sequenceName = "INSTITUTE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instituteSeq")
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    public Institute(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return String.format("id: {%d}. Name: {%s}", id, name);
    }

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "institutes")
    private Set<ProjectConsortium> projectConsortia;
}
