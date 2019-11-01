/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package uk.ac.ebi.impc_prod_tracker.web.mapping.target_gene_list;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.target_gene_list.ConsortiumList;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.web.dto.target_gene_list.ConsortiumListDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.target_gene_list.TargetListsByConsortiumDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TargetListsByConsortiumMapper
{
    private ConsortiumListMapper consortiumListMapper;

    public TargetListsByConsortiumMapper(ConsortiumListMapper consortiumListMapper)
    {
        this.consortiumListMapper = consortiumListMapper;
    }

    public List<TargetListsByConsortiumDTO> consortiumListsToDtos(
        Collection<ConsortiumList> consortiumLists)
    {
        List<TargetListsByConsortiumDTO> targetListsByConsortiumDTOS = new ArrayList<>();

        Map<Consortium, List<ConsortiumList>> listsByConsortium =
            getListByConsortiumMap(consortiumLists);

        listsByConsortium.forEach(
            (k, v) -> targetListsByConsortiumDTOS.add(buildListByConsortium(k, v)));

        return targetListsByConsortiumDTOS;
    }

    private TargetListsByConsortiumDTO buildListByConsortium(
        Consortium consortium, List<ConsortiumList> consortiumLists)
    {
        TargetListsByConsortiumDTO targetListsByConsortiumDTO = new TargetListsByConsortiumDTO();
        targetListsByConsortiumDTO.setConsortiumName(consortium.getName());
        List<ConsortiumListDTO> consortiumListDTOs = consortiumListMapper.toDtos(consortiumLists);
        targetListsByConsortiumDTO.setConsortiumListDTOList(consortiumListDTOs);
        return targetListsByConsortiumDTO;
    }

    private Map<Consortium, List<ConsortiumList>> getListByConsortiumMap(
        Collection<ConsortiumList> consortiumLists)
    {
        Map<Consortium, List<ConsortiumList>> listsByConsortium = new HashMap<>();
        if (consortiumLists != null)
        {
            consortiumLists.forEach(x ->
            {
                var currentConsortium = x.getConsortium();
                if (listsByConsortium.containsKey(currentConsortium))
                {
                    var currentList = listsByConsortium.get(currentConsortium);
                    currentList.add(x);
                }
                else
                {
                    List<ConsortiumList> listByConsortium = new ArrayList<>();
                    listByConsortium.add(x);
                    listsByConsortium.put(x.getConsortium(), listByConsortium);
                }
            });
        }
        return listsByConsortium;
    }
}
