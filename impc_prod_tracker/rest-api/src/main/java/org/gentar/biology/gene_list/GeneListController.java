/* *************************************************************************
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

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.biology.gene_list.filter.GeneListFilter;
import org.gentar.biology.gene_list.filter.GeneListFilterBuilder;
import org.gentar.biology.gene_list.record.GeneListProjection;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.biology.gene_list.record.ListRecordTypeService;
import org.gentar.helpers.*;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.consortium.ConsortiumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    private final ListRecordTypeService listRecordTypeService;
    private final CsvReader csvReader;
    private final CsvWriter<SearchCsvRecord> csvWriter;
    private final GeneExternalService geneExternalService;
    private final GeneListCsvRecordMapper geneListCsvRecordMapper;
    private final ConsortiumService consortiumService;
    private final EntityManager entityManager;

    public GeneListController(
            GeneListService geneListService,
            GeneListMapper geneListMapper,
            ListRecordMapper listRecordMapper,
            ListRecordTypeService listRecordTypeService, CsvReader csvReader,
            CsvWriter<SearchCsvRecord> csvWriter,
            GeneExternalService geneExternalService,
            GeneListCsvRecordMapper geneListCsvRecordMapper,
            ConsortiumService consortiumService,
            EntityManager entityManager)
    {
        this.geneListService = geneListService;
        this.geneListMapper = geneListMapper;
        this.listRecordMapper = listRecordMapper;
        this.listRecordTypeService = listRecordTypeService;
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
            converted.forEach((k, v) -> accIds.add(v));
        }

        return accIds;
    }

    private ResponseEntity<?> buildResponseEntity(
        PagedResourcesAssembler assembler,
        String slashContent, Page<ListRecord> geneListRecordsPage)
    {
        Page<ListRecordDTO> geneListRecordDTOPage =
            geneListRecordsPage.map(listRecordMapper::toDto);
        Link link = linkTo(GeneListController.class).slash(slashContent).withSelfRel();
        link = link.withHref(decode(link.getHref()));
        PagedModel pr =
            assembler.toModel(
                geneListRecordDTOPage,
                link);
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

        PrintWriter printWriter = response.getWriter();
        csvWriter.writeListToCsv(printWriter, geneListCsvRecords, GeneListCsvRecord.HEADERS);
        if (printWriter != null){
            printWriter.flush();
            printWriter.close();
        }

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

    @GetMapping("/{consortiumName}/exportProjection")
    @Transactional(readOnly = true)
    public void exportProjection(
            HttpServletResponse response,
            @PathVariable("consortiumName") String consortiumName)
    {
        List<GeneListProjection> glp = geneListService.getGeneListProjectionsByConsortiumName(consortiumName);
        Map<Long, List<GeneListProjection>> listRecordGenes = glp.stream().collect(Collectors.groupingBy(GeneListProjection::getId));
        //System.out.println(listRecordGenes.containsKey(1L));
        System.out.println(listRecordGenes.get(1L).getFirst());

        Map<String, Set<String>> geneProjectsSet = glp.stream().collect(Collectors.groupingBy(GeneListProjection::getSymbol, Collectors.mapping(GeneListProjection::getTpn, Collectors.toSet())));
        Map<String, Set<String>> projectSummaryStatus = glp.stream().collect(Collectors.groupingBy(GeneListProjection::getTpn, Collectors.mapping(GeneListProjection::getSummaryStatus, Collectors.toSet())));
        System.out.println(geneProjectsSet.containsKey("Otog"));
        System.out.println(List.copyOf(geneProjectsSet.get("Otog")));
        geneProjectsSet.forEach((k, v) -> System.out.println(k + " : " + v.size() + " : " + List.copyOf(v)));

        Map<Long, List<String>> listRecordTypeByListRecord = listRecordTypeService.getRecordTypesByListRecord();

        listRecordGenes.forEach((k,v) -> {
                List<String> geneSymbols = listRecordGenes.get(k).stream().map(GeneListProjection::getSymbol).distinct().collect(Collectors.toList());
                List<String> projectList = getProjectsForListRecord(k,geneSymbols, geneProjectsSet);
                projectList.forEach(tpn ->
                    System.out.println(String.join(",", geneSymbols) +
                            "\t" + listRecordGenes.get(k).getFirst().getNote() +
                            "\t" + listRecordTypeByListRecord.get(k).stream().distinct().collect(Collectors.joining(",")) +
                            "\t" + listRecordGenes.get(k).getFirst().getVisible() +
                            "\t" + tpn +
                            "\t" + List.copyOf(projectSummaryStatus.get(tpn)).getFirst()
                    )

                );
        });

    }

    private List<String> getProjectsForListRecord(Long listRecordId,
                                                  List<String> geneSymbols,
                                                  Map<String, Set<String>> geneProjectsSet) {
        List<String> projectList = new ArrayList<>();

        if (geneSymbols.size() == 1) {
            projectList = List.copyOf(geneProjectsSet.get(geneSymbols.getFirst()));
        } else if (geneSymbols.size() > 1) {
            List<Set<String>> listOfProjectSets  = new ArrayList<>();
            geneSymbols.forEach(s -> listOfProjectSets.add(geneProjectsSet.get(s)));
            for (int i = 1; i < listOfProjectSets.size(); i++) {
                listOfProjectSets.getFirst().retainAll(listOfProjectSets.get(i));
            }
            projectList = List.copyOf(geneProjectsSet.get(geneSymbols.getFirst()));
        }
        return projectList;
    }

    private void setCsvParams(final HttpServletResponse response)
    {
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
        if (printWriter != null){
            printWriter.flush();
            printWriter.close();
        }

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

        PrintWriter printWriter = response.getWriter();
        csvWriter.writeListToCsv(printWriter, geneListCsvRecords, GeneListCsvRecord.HEADERS);
        if (printWriter != null) {
            printWriter.flush();
            printWriter.close();
        }
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
