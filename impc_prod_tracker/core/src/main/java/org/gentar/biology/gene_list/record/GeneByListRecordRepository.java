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
package org.gentar.biology.gene_list.record;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GeneByListRecordRepository extends CrudRepository<GeneByListRecord, Long>
{
    @Query(
        value = "select glr.acc_id from Gene_by_list_record glr, list_record lr where lr.id = glr.list_record_id and lr.gene_list_id = ?1",
        nativeQuery = true)
    List<Object> getAllAccIdsByConsortiumId(Long consortiumId);
}
