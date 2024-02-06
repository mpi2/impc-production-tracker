/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.mutation.molecular_type.MolecularMutationTypeService;
import org.springframework.stereotype.Component;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;

@Component
public class MolecularMutationTypeMapper implements Mapper<MolecularMutationType, String>
{
    private final MolecularMutationTypeService molecularMutationTypeService;

    public MolecularMutationTypeMapper(MolecularMutationTypeService molecularMutationTypeService)
    {
        this.molecularMutationTypeService = molecularMutationTypeService;
    }

    @Override
    public String toDto(MolecularMutationType entity) {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    public MolecularMutationType toEntity(String molecularMutationTypeName)
    {
        return molecularMutationTypeService.getMolecularMutationTypeByNameFailingWhenNull(
            molecularMutationTypeName);
    }
}
