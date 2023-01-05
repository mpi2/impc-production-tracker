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
package org.gentar.organization.person.associations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.person.Person;
import org.gentar.organization.role.Role;
import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
@Table(uniqueConstraints={
    @UniqueConstraint(columnNames = {"person_id", "consortium_id", "role_id"})
})
public class PersonRoleConsortium extends BaseEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "personRoleConsortiumSeq", sequenceName = "PERSON_ROLE_CONSORTIUM_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personRoleConsortiumSeq")
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = Person.class)
    private Person person;

    @NotNull
    @ManyToOne(targetEntity = Consortium.class)
    private Consortium consortium;

    @NotNull
    @ManyToOne(targetEntity = Role.class)
    private Role role;
}
