/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
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
package org.gentar.biology.colony.distribution_network;

import org.gentar.biology.colony.distribution.distribution_network.DistributionNetwork;
import org.gentar.biology.colony.distribution.distribution_network.DistributionNetworkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/distributionNetwork")
public class DistributionNetworkController {

    private final DistributionNetworkService distributionNetworkService;

    public DistributionNetworkController(DistributionNetworkService distributionNetworkService) {
        this.distributionNetworkService = distributionNetworkService;
    }

    @GetMapping(value = {"/findAllNames"})
    public List<String> getAlldistributionNetworkNames() {
        return distributionNetworkService.getAllDistributionNetworks().stream().map(DistributionNetwork::getName).toList();
    }


}
