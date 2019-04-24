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
package uk.ac.ebi.impc_prod_tracker.data.organization.institute;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@EqualsAndHashCode( exclude = {"people"}, callSuper = false)
@Entity
public class Institute extends BaseEntity {

    @Id
    @SequenceGenerator(name = "instituteSeq", sequenceName = "INSTITUTE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instituteSeq")
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToMany
    @JoinTable(
        name = "institute_person",
        joinColumns = @JoinColumn(name = "institute_id"),
        inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> people = new HashSet<>();

    @ManyToMany(mappedBy = "institutes")
    private Set<Consortium> consortia;

    public Institute(String name)
    {
        this.name = name;
    }

    public void addPerson(Person person)
    {
        this.people.add(person);
        person.getInstitutes().add(this);
    }

    public void removePerson(Person person)
    {
        this.people.remove(person);
        person.getInstitutes().remove(this);
    }

    public String toString()
    {
        return String.format("id: {%d}. Name: {%s}", id, name);
    }
}
