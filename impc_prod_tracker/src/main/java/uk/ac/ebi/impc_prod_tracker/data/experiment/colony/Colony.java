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
package uk.ac.ebi.impc_prod_tracker.data.experiment.colony;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.breeding_attempt.BreedingAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_strain.TrackedStrain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class Colony extends BaseEntity {
    @Id
    @SequenceGenerator(name = "colonySeq", sequenceName = "COLONY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "colonySeq")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    private TrackedStrain strain;

    @ManyToMany
    @JoinTable(
        name = "colony_relationship",
        joinColumns = @JoinColumn(name = "colony_id"),
        inverseJoinColumns = @JoinColumn(name = "parent_colony_id"))
    private Set<Colony> parentColonies;

    @ManyToMany(mappedBy = "parentColonies")
    private Set<Colony> childColonies;

    @ManyToMany(mappedBy = "colonies")
    private Set<BreedingAttempt> breedingAttempts;
}
