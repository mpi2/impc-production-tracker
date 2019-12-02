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
package org.gentar.biology.gene_list;

import org.gentar.biology.gene_list.record.GeneByListRecord;
import org.springframework.stereotype.Component;
import org.gentar.biology.gene_list.record.GeneByListRecordRepository;

@Component
public class GeneByGeneListRecordService
{
    private GeneByListRecordRepository geneByListRecordRepository;

    public GeneByGeneListRecordService(
        GeneByListRecordRepository geneByListRecordRepository)
    {
        this.geneByListRecordRepository = geneByListRecordRepository;
    }

    public GeneByListRecord findById(Long id)
    {
        return geneByListRecordRepository.findById(id).orElse(null);
    }
}
