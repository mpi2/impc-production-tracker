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
import org.gentar.helpers.CsvWriter;
import org.gentar.helpers.GeneListCsvRecord;
import org.gentar.helpers.LinkUtil;
import org.gentar.helpers.SearchCsvRecord;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.consortium.ConsortiumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/geneList")
@CrossOrigin(origins = "*")
public class GeneListController
{
    private final GeneListService geneListService;
    private final GeneListMapper geneListMapper;
    private final ListRecordMapper listRecordMapper;
    private final CsvReader csvReader;
    private final CsvWriter<SearchCsvRecord> csvWriter;
    private final GeneExternalService geneExternalService;
    private final GeneListCsvRecordMapper geneListCsvRecordMapper;
    private final ConsortiumService consortiumService;
    private final EntityManager entityManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneListController.class);

    public GeneListController(
        GeneListService geneListService,
        GeneListMapper geneListMapper,
        ListRecordMapper listRecordMapper,
        CsvReader csvReader,
        CsvWriter<SearchCsvRecord> csvWriter,
        GeneExternalService geneExternalService,
        GeneListCsvRecordMapper geneListCsvRecordMapper,
        ConsortiumService consortiumService,
        EntityManager entityManager)
    {
        this.geneListService = geneListService;
        this.geneListMapper = geneListMapper;
        this.listRecordMapper = listRecordMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
        this.geneExternalService = geneExternalService;
        this.geneListCsvRecordMapper = geneListCsvRecordMapper;
        this.consortiumService = consortiumService;
        this.entityManager = entityManager;
    }

    /**
     * Get the list of gene lists in the system.
     *
     * @return List of information about all the gene lists.
     */
    @GetMapping(value = {"/descriptions"})
    public ResponseEntity<?> getGeneListsDescriptions()
    {
        List<GeneList> geneLists = geneListService.getAll();
        List<GeneListDTO> geneListDTOS = geneListMapper.toDtos(geneLists);
        return new ResponseEntity<>(geneListDTOS, HttpStatus.OK);
    }

    /**
     * Get target gene lists by consortium.
     *
     * @param pageable       Pagination information.
     * @param assembler      Allows to manage hal.
     * @param consortiumName Name of the consortium.
     * @return Lists by consortium.
     */
    @GetMapping(value = {"/{consortiumName}/content"})
    public ResponseEntity<?> findByConsortium(
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

    /**
     * Get target gene lists by consortium.
     *
     * @param pageable       Pagination information.
     * @param assembler      Allows to manage hal.
     * @param consortiumName Name of the consortium.
     * @return Lists by consortium.
     */
    @GetMapping(value = {"/{consortiumName}/publicContent"})
    public ResponseEntity<?> findPublicRecordsByConsortium(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @PathVariable("consortiumName") String consortiumName,
        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols)
    {
        List<String> accIds = getListAccIdsByMarkerSymbols(markerSymbols);
        GeneListFilter filter = GeneListFilterBuilder.getInstance()
            .withConsortiumName(consortiumName)
            .withAccIds(accIds)
            .withVisible(Boolean.TRUE)
            .build();

        Page<ListRecord> geneListRecords =
            geneListService.getAllWithFilters(pageable, filter);
        String slashContent = consortiumName + "/publicContent";
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

    private ResponseEntity<?> buildResponseEntity(
        PagedResourcesAssembler assembler,
        String slashContent, Page<ListRecord> geneListRecordsPage)
    {
        Page<ListRecordDTO> geneListRecordDTOPage =
            geneListRecordsPage.map(listRecordMapper::toDto);
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
     *
     * @param pageable       Pagination information.
     * @param assembler      Assembler to create links.
     * @param records        Records to update.
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
        records.forEach(x -> x.setConsortiumName(consortiumName));
        List<ListRecord> listRecords =
            new ArrayList<>(listRecordMapper.toEntities(records));
        geneListService.updateRecordsInList(listRecords, consortiumName);
        return findByConsortium(pageable, assembler, consortiumName, Collections.emptyList());
    }

    /**
     * Delete records of a list
     *
     * @param pageable       Pagination information.
     * @param assembler      Assembler to create links.
     * @param recordsIds     Records to delete.
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

    @GetMapping("/{consortiumName}/export")
    public void exportCsv(
        HttpServletResponse response,
        @PathVariable("consortiumName") String consortiumName,
        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols) throws IOException
    {
        String filename = "download.csv";
        response.setContentType("text/csv");
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        List<String> accIds = getListAccIdsByMarkerSymbols(markerSymbols);
        GeneListFilter filter = GeneListFilterBuilder.getInstance()
            .withConsortiumName(consortiumName)
            .withAccIds(accIds)
            .build();
        List<ListRecord> records =
            geneListService.getAllNotPaginatedWithFilters(filter);
        records.sort(Comparator.comparing(ListRecord::getId));
        List<GeneListCsvRecord> geneListCsvRecords = geneListCsvRecordMapper.toDtos(records);
        csvWriter.writeListToCsv(response.getWriter(), geneListCsvRecords, GeneListCsvRecord.HEADERS);
    }

    @GetMapping("/{consortiumName}/exportStream")
    @Transactional(readOnly = true)
    public void exportCsvStream(
        HttpServletResponse response,
        @PathVariable("consortiumName") String consortiumName,
        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols) throws IOException
    {
        String filename = "download.csv";
        response.setContentType("text/csv");
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        List<String> accIds = getListAccIdsByMarkerSymbols(markerSymbols);
        GeneListFilter filter = GeneListFilterBuilder.getInstance()
            .withConsortiumName(consortiumName)
            .withAccIds(accIds)
            .build();
        Stream<ListRecord> recordsStream = geneListService.getAllStream(filter);

        Consortium consortium = consortiumService.getConsortiumByNameOrThrowException(consortiumName);

        writeCustomersToResponseAsCsv(recordsStream, response, consortium.getId());
    }

    private void setCsvParams(final HttpServletResponse response)
    {
        // not important; basically sets csv params so clients can understand it's a csv
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment;filename=customers.csv");
    }

    @Transactional
        // doesn't do anything; just a reminder that this requires a transaction
    void writeCustomersToResponseAsCsv(
        Stream<ListRecord> listRecordStream, final HttpServletResponse response, Long consortiumId)
        throws IOException
    {
        setCsvParams(response);
        PrintWriter printWriter = response.getWriter();
        csvWriter.csvWriterOneByOne(printWriter, GeneListCsvRecord.HEADERS);

        List<String> allAccIdsByConsortium = geneListService.getAllAccIdsByConsortiumId(consortiumId);
        geneListCsvRecordMapper.initMap(allAccIdsByConsortium);
        listRecordStream.peek(listRecord ->
            {
                GeneListCsvRecord geneListCsvRecord = geneListCsvRecordMapper.toDto(listRecord);
                String[] rowAsArray = geneListCsvRecord.getRowAsArray();
                csvWriter.csvWriterOneByOne(printWriter, rowAsArray);
            }
        )
            .forEach(entityManager::detach);
        printWriter.flush();
        printWriter.close();
    }

    @GetMapping("/{consortiumName}/exportPublic")
    public void exportPublicRecordsCsv(
        HttpServletResponse response,
        @PathVariable("consortiumName") String consortiumName,
        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols) throws IOException
    {
        String filename = "download.csv";
        response.setContentType("text/csv");
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        List<String> accIds = getListAccIdsByMarkerSymbols(markerSymbols);
        GeneListFilter filter = GeneListFilterBuilder.getInstance()
            .withConsortiumName(consortiumName)
            .withAccIds(accIds)
            .withVisible(Boolean.TRUE)
            .build();
        List<ListRecord> records =
            geneListService.getAllNotPaginatedWithFilters(filter);
        records.sort(Comparator.comparing(ListRecord::getId));
        List<GeneListCsvRecord> geneListCsvRecords = geneListCsvRecordMapper.toDtos(records);
        csvWriter.writeListToCsv(response.getWriter(), geneListCsvRecords, GeneListCsvRecord.HEADERS);
    }
}
