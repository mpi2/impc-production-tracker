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
package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.production_plan;

import org.springframework.hateoas.server.EntityLinks;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.ContextAwarePolicyEnforcement;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.ProductionPlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.AttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.MutagenesisDonorDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.crispr_attempt.CrisprAttemptDTO;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.mutagenesis_donor.MutagenesisDonor;
import java.util.List;

/**
 * This class adds the corresponding links to the subresources in a ProductionPlanDTO object
 */
@Component
public class ProductionPlanDTOLinkManager
{
    private ContextAwarePolicyEnforcement policy;
    private final EntityLinks entityLinks;

    public ProductionPlanDTOLinkManager(ContextAwarePolicyEnforcement policy, EntityLinks entityLinks)
    {
        this.policy = policy;
        this.entityLinks = entityLinks;
    }

    public ProductionPlanDTO addLinks(ProductionPlanDTO productionPlanDTO)
    {
        AttemptDTO attempt = productionPlanDTO.getAttempt();
        if (attempt != null)
        {
            CrisprAttemptDTO crisprAttempt = attempt.getCrisprAttempt();
            if (crisprAttempt != null)
            {
                addLinksToCrisprAttempt(crisprAttempt);
            }
        }
        return productionPlanDTO;
    }

    private void addLinksToCrisprAttempt(CrisprAttemptDTO crisprAttemptDTO)
    {
        crisprAttemptDTO.add(
        entityLinks.linkToCollectionResource(
            MutagenesisDonor.class).withRel("_mutagenesisDonors"));
        addLinksToMutagenesisDonor(crisprAttemptDTO.getMutagenesisDonors());


    }
    private void addLinksToMutagenesisDonor(List<MutagenesisDonorDTO> mutagenesisDonorDTOList)
    {
        if (!policy.hasPermission(null, "DELETE_RESOURCE"))
        {
            return;
        }
        for (MutagenesisDonorDTO mutagenesisDonorDTO : mutagenesisDonorDTOList)
        {
            addLinksToMutagenesisDonor(mutagenesisDonorDTO);
        }
    }

    private void addLinksToMutagenesisDonor(MutagenesisDonorDTO mutagenesisDonorDTO)
    {
        mutagenesisDonorDTO.add(
            entityLinks.linkForItemResource(
                MutagenesisDonor.class, mutagenesisDonorDTO.getId()).withRel("delete"));
    }
}
