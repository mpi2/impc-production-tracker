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
package org.gentar.entity_values;

import java.util.List;

public interface EntitiesValuesByUserService
{
    /**
     * Returns a catalog with data for entities according to the user. The user must be manager or
     * admin because the entities are meant to be data managed by that user.
     * @return Map with entities and values for those entities.
     */
    List<EntityValues> getListsByManagerUser();
}
