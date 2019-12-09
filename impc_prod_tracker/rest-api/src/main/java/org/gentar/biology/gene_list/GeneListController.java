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

import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.biology.gene_list.filter.GeneListFilter;
import org.gentar.biology.gene_list.filter.GeneListFilterBuilder;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.helpers.CsvReader;
import org.gentar.helpers.LinkUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/geneList")
@CrossOrigin(origins = "*")
public class GeneListController
{
    private GeneListService geneListService;
    private ListRecordMapper listRecordMapper;
    private CsvReader csvReader;
    private GeneExternalService geneExternalService;

    public GeneListController(
        GeneListService geneListService,
        ListRecordMapper listRecordMapper,
        CsvReader csvReader, GeneExternalService geneExternalService)
    {
        this.geneListService = geneListService;
        this.listRecordMapper = listRecordMapper;
        this.csvReader = csvReader;
        this.geneExternalService = geneExternalService;
    }

    /**
     * Get target gene lists by consortium.
     * @param pageable Pagination information.
     * @param assembler Allows to manage hal.
     * @param consortiumName Name of the consortium.
     * @return Lists by consortium.
     */
    @GetMapping(value = {"/{consortiumName}/content"})
    public ResponseEntity findByConsortium(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @PathVariable("consortiumName") String consortiumName,
        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols)
    {
        List<String> accIds = getListAccIdsByMarkerSymbols(markerSymbols);
        if (markerSymbols != null && accIds.isEmpty())
        {
            return buildNoContent();
        }
        GeneListFilter filter = GeneListFilterBuilder.getInstance()
            .withConsortiumName(consortiumName)
            .withAccIds(accIds)
            .build();

        Page<ListRecord> geneListRecords =
            geneListService.getAllWithFilters(pageable, filter);
        String slashContent = consortiumName + "/content";
        return buildResponseEntity(assembler, slashContent, geneListRecords);
    }

    List<String> getListAccIdsByMarkerSymbols(List<String> markerSymbols)
    {
        List<String> accIds = new ArrayList<>();
        if (markerSymbols != null)
        {
            Map<String, String> converted =
                geneExternalService.getAccIdsByMarkerSymbols(markerSymbols);
            converted.forEach((k, v) -> {
                accIds.add(v);
            });
        }

        return accIds;
    }

    private ResponseEntity buildResponseEntity(
        PagedResourcesAssembler assembler,
        String slashContent, Page<ListRecord> geneListRecordsPage)
    {
        Page<ListRecordDTO> geneListRecordDTOPage =
            geneListRecordsPage.map(x -> listRecordMapper.toDto(x));
        PagedModel pr =
            assembler.toModel(
                geneListRecordDTOPage,
                linkTo(GeneListController.class).slash(slashContent).withSelfRel());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }

    private ResponseEntity buildNoContent()
    {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/updateListWithFile/{consortiumName}")
    public ResponseEntity uploadFile(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestParam("file") MultipartFile file,
        @PathVariable("consortiumName") String consortiumName)
    {
        List<List<String>> csvContent = csvReader.getCsvContentFromMultipartFile(file);
        geneListService.updateListWithCsvContent(consortiumName, csvContent);
        return findByConsortium(pageable, assembler, consortiumName, Collections.emptyList());
    }

    /**
     * Update the content of a list
     * @param pageable Pagination information.
     * @param assembler Assembler to create links.
     * @param records Records to update.
     * @param consortiumName Name of the consortium
     * @return Paginated content updated.
     */
    @PostMapping(value = {"/{consortiumName}/content"})
    public ResponseEntity updateRecordsInList(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestBody List<ListRecordDTO> records,
        @PathVariable("consortiumName") String consortiumName)
    {
        List<ListRecord> listRecords =
            new ArrayList<>(listRecordMapper.toEntities(records));
        geneListService.updateRecordsInList(listRecords, consortiumName);
        return findByConsortium(pageable, assembler, consortiumName, Collections.emptyList());
    }

    /**
     * Delete records of a list
     * @param pageable Pagination information.
     * @param assembler Assembler to create links.
     * @param recordsIds Records to delete.
     * @param consortiumName Name of the consortium
     * @return Paginated content updated.
     */
    @DeleteMapping(value = {"/{consortiumName}/content"})
    public ResponseEntity deleteRecordsInList(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestParam(value = "recordId", required = false) List<Long> recordsIds,
        @PathVariable("consortiumName") String consortiumName)
    {
        geneListService.deleteRecordsInList(recordsIds, consortiumName);
        return findByConsortium(pageable, assembler, consortiumName, Collections.emptyList());
    }
}
