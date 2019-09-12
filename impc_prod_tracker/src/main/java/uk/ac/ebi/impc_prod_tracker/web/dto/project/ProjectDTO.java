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
package uk.ac.ebi.impc_prod_tracker.web.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import uk.ac.ebi.impc_prod_tracker.web.dto.gene.ProjectGeneDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.location.ProjectLocationDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.species.SpeciesDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.status_stamps.StatusStampsDTO;

import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ProjectDTO extends RepresentationModel
{
    @NonNull
    private String tpn;

    @NonNull
    @JsonProperty("assigment_status_name")
    private String assigmentStatusName;

    @JsonProperty("assignment_status_stamps")
    private List<StatusStampsDTO> statusStampsDTOS;

    @JsonProperty("external_reference")
    private String externalReference;

    private Boolean withdrawn;
    private Boolean recovery;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("intention_by_gene_attributes")
    private List<ProjectGeneDTO> projectGeneDTOS;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("intention_by_location_attributes")
    private List<ProjectLocationDTO> projectLocationDTOS;

    @JsonIgnore
    private Long imitsMiPlanId;

    private String comment;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("privacy_name")
    private String privacyName;

    @JsonProperty("species_attributes")
    private List<SpeciesDTO> projectSpeciesDTOs;

    @JsonProperty("consortia_names")
    private List<String> consortiaNames;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("is_object_restricted")
    private Boolean isObjectRestricted;
}
