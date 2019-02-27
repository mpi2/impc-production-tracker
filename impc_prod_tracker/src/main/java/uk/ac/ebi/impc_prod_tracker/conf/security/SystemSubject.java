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
package uk.ac.ebi.impc_prod_tracker.conf.security;

import uk.ac.ebi.impc_prod_tracker.domain.ProductionCentre;
import uk.ac.ebi.impc_prod_tracker.domain.UserRole;

/**
 * Information a subject in the system should have.
 * @author Mauricio Martinez
 */
public interface SystemSubject
{
    String getLogin();

    String getName();

    String getUserRefId();

    String getEmail();

    UserRole getRole();

    ProductionCentre getProductionCentre();

    ProductionCentre loadProductionCentre(String login);
}
