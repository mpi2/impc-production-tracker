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
package org.gentar.web.mapping.privacy;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.service.biology.project.privacy.PrivacyService;
import org.springframework.stereotype.Component;
import org.gentar.biology.privacy.Privacy;

@Component
public class PrivacyMapper
{
    private PrivacyService privacyService;

    private static final String PRIVACY_NOT_FOUND_ERROR = "Privacy [%s] does not exist.";

    public PrivacyMapper(PrivacyService privacyService)
    {
        this.privacyService = privacyService;
    }

    public Privacy toEntity(String privacyName)
    {
        Privacy privacy = privacyService.findPrivacyByName(privacyName);
        if (privacy == null)
        {
            throw new UserOperationFailedException(String.format(PRIVACY_NOT_FOUND_ERROR, privacyName));
        }
        return privacy;
    }
}
