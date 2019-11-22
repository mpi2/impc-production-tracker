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
package org.gentar.web.controller.conf;

import org.gentar.service.conf.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * Provides information recurrently needed as work groups, work units, etc.
 *
 * @author Mauricio Martinez
 */
@RestController
@RequestMapping("/api")
public class ConfigurationController
{
    private CatalogService catalogService;

    public ConfigurationController(CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }

    @GetMapping(value = {"/conf"})
    public Map<String, List<String>> getConfiguration()
    {
        return catalogService.getCatalog();
    }
}
