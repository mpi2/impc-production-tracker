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
package uk.ac.ebi.impc_prod_tracker.data.biology.human_disease;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.biology.omim_table.OmimTable;
import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class HumanDisease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    private String name;

    private String doId;


    @ManyToMany
    @JoinTable(
        name = "human_disease_omim",
        joinColumns = @JoinColumn(name = "human_disease_id"),
        inverseJoinColumns = @JoinColumn(name = "omim_table_id"))
    private Set<OmimTable> omimTable;


}
