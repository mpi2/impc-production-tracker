/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package uk.ac.ebi.impc_prod_tracker.data.organization.person;

import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.organization.person_role_consortium.PersonRoleConsortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.person_role_work_unit.PersonRoleWorkUnit;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
@RestResource(rel = "people", path = "people")
public class Person extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "personSeq", sequenceName = "PERSON_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSeq")
    private Long id;

    private String name;

    private String authId;

    private Boolean isActive;

    private Boolean contactable;

    // The password is not saved in the database.
    transient private String password;

    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "^(.+)@(.+)$", message = "Invalid email format")
    private String email;

    private Boolean ebiAdmin;

    public Person(String email)
    {
        this.email = email;
    }

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "person", orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<PersonRoleWorkUnit> rolesWorkUnits;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "person", orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<PersonRoleConsortium> rolesConsortia;

    @Override
    public String toString()
    {
        return String.format("id: {%s}, email: {%s}", id, email);
    }
}
