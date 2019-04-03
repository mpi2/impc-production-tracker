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
package uk.ac.ebi.impc_prod_tracker.data.organization.person;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
@RestResource(rel = "people", path = "people")
public class Person extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "personSeq", sequenceName = "PERSON_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSeq")
    private Long id;

    private String authId;

    @ManyToOne
    @JoinColumn
    @NotNull
    private Role role;

    private Boolean isActive;

    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "^(.+)@(.+)$", message = "Invalid email format")
    private String email;

    @ManyToOne
    @JoinColumn
    private WorkUnit workUnit;

    @ManyToMany(mappedBy = "people")
    private Set<Institute> institutes;

    public Person(String email)
    {
        this.email = email;
    }
}
