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

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ProjectDetailsDTO extends RepresentationModel
{
    @NonNull
    private String tpn;
    @NonNull
    private String assigmentStatusName;
    private List<String> locations;
    private List<String> alleleIntentions;
    private List<MarkerSymbolDTO> markerSymbols;
    private List<HumanGeneSymbolDTO> humanGenes;

    @Data
    public static class MarkerSymbolDTO
    {
        String markerSymbol;
        String mgiLink;
    }

    @Data
    public static class HumanGeneSymbolDTO
    {
        String symbol;
        String support;
        Long support_count;
    }
}
