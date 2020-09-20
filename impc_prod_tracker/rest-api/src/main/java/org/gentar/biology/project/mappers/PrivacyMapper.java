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
package org.gentar.biology.project.mappers;

import org.gentar.Mapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.biology.project.privacy.PrivacyService;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.privacy.Privacy;

@Component
public class PrivacyMapper implements Mapper<Privacy, String>
{
    private PrivacyService privacyService;

    private static final String PRIVACY_NOT_FOUND_ERROR = "Privacy '%s' does not exist.";
    private static final String PRIVACY_NULL_ERROR = "Privacy cannot be '%s'.";

    public PrivacyMapper(PrivacyService privacyService)
    {
        this.privacyService = privacyService;
    }

    public String toDto(Privacy entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    public Privacy toEntity(String privacyName)
    {
        if (privacyName == null)
        {
            throw new UserOperationFailedException(String.format(PRIVACY_NULL_ERROR, privacyName));
        }
        Privacy privacy = privacyService.findPrivacyByName(privacyName);
        if (privacy == null)
        {
            throw new UserOperationFailedException(String.format(PRIVACY_NOT_FOUND_ERROR, privacyName));
        }
        return privacy;
    }
}
