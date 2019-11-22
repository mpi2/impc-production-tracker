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
package org.gentar.web.dto.intention;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.gentar.web.dto.gene.ProjectIntentionGeneDTO;
import org.gentar.web.dto.sequence.ProjectIntentionSequenceDTO;
import org.gentar.web.dto.allele_categorization.AlleleCategorizationDTO;

import java.util.List;

@Data
public class ProjectIntentionDTO
{
    private String alleleTypeName;
    private String molecularMutationTypeName;

    @JsonProperty("alleleCategorizations")
    private List<AlleleCategorizationDTO> alleleCategorizationDTOS;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("intentionByGene")
    private ProjectIntentionGeneDTO projectIntentionGeneDTO;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("intentionBySequence")
    private ProjectIntentionSequenceDTO projectIntentionSequenceDTO;

    private String intentionTypeName;

    @JsonIgnore
    private int index;
}
