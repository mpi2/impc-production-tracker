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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.gentar.biology.gene_list.record.GeneListRecord;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/geneList")
@CrossOrigin(origins = "*")
public class GeneListController
{
    private GeneListService geneListService;
    private GeneListRecordMapper geneListRecordMapper;
    private CsvReader csvReader;

    public GeneListController(
        GeneListService geneListService,
        GeneListRecordMapper geneListRecordMapper,
        CsvReader csvReader)
    {
        this.geneListService = geneListService;
        this.geneListRecordMapper = geneListRecordMapper;
        this.csvReader = csvReader;
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
        @PathVariable("consortiumName") String consortiumName)
    {
        Page<GeneListRecord> geneListRecords =
            geneListService.getByConsortium(pageable, consortiumName);
        String slashContent = consortiumName + "/content";
        return buildResponseEntity(assembler, slashContent, geneListRecords);
    }

    private ResponseEntity buildResponseEntity(
        PagedResourcesAssembler assembler,
        String slashContent, Page<GeneListRecord> geneListRecordsPage)
    {
        Page<GeneListRecordDTO> geneListRecordDTOPage =
            geneListRecordsPage.map(x -> geneListRecordMapper.toDto(x));
        PagedModel pr =
            assembler.toModel(
                geneListRecordDTOPage,
                linkTo(GeneListController.class).slash(slashContent).withSelfRel());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
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
        return findByConsortium(pageable, assembler, consortiumName);
    }

    @PostMapping(value = {"/{consortiumName}/content"})
    public ResponseEntity updateRecordsInList(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @RequestBody List<GeneListRecordDTO> records,
        @PathVariable("consortiumName") String consortiumName)
    {
        List<GeneListRecord> geneListRecords =
            new ArrayList<>(geneListRecordMapper.toEntities(records));
        geneListService.updateRecordsInList(geneListRecords, consortiumName);
        return findByConsortium(pageable, assembler, consortiumName);
    }
}
