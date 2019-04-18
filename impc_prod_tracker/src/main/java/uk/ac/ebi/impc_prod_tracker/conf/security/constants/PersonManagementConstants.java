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
package uk.ac.ebi.impc_prod_tracker.conf.security.constants;

import org.springframework.beans.factory.annotation.Value;

public class PersonManagementConstants
{
    @Value("${local_authentication_url}")
    public static final String LOCAL_AUTHENTICATION_URL = "https://explore.api.aai.ebi.ac.uk/auth";

    private PersonManagementConstants()
    {
        // Prevent instantiation
    }
    public static final String ADMIN_ROLE = "admin";
}
