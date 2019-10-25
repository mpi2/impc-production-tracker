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
package uk.ac.ebi.impc_prod_tracker.service.biology.project.privacy;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.Privacy;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.PrivacyRepository;

@Component
public class PrivacyService
{
    private PrivacyRepository privacyRepository;

    public PrivacyService(PrivacyRepository privacyRepository)
    {
        this.privacyRepository = privacyRepository;
    }

    public Privacy findPrivacyByName(String name)
    {
        return privacyRepository.findByNameIgnoreCase(name);
    }
}
