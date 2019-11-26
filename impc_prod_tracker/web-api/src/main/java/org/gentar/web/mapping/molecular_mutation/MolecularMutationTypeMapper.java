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
package org.gentar.web.mapping.molecular_mutation;

import org.gentar.service.biology.molecular_mutation.MolecularMutationService;
import org.springframework.stereotype.Component;
import org.gentar.biology.molecular_mutation_type.MolecularMutationType;

@Component
public class MolecularMutationTypeMapper
{
    private MolecularMutationService molecularMutationService;

    public MolecularMutationTypeMapper(MolecularMutationService molecularMutationService)
    {
        this.molecularMutationService = molecularMutationService;
    }

    public MolecularMutationType toEntity(String molecularMutationTypeName)
    {
        return molecularMutationService.getMolecularMutationTypeByName(molecularMutationTypeName);
    }
}