package org.gentar.biology.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import org.gentar.biology.ortholog.OrthologService;
import org.gentar.biology.project.descriptor.ProjectSearchDownloadMapper;
import org.gentar.biology.project.engine.ProjectValidator;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.biology.project.projection.dto.ProjectSearchDownloadOrthologDto;
import org.gentar.biology.project.projection.dto.ProjectSearchDownloadProjectionDto;
import org.gentar.biology.project.projection.dto.ProjectSearchDownloadProjectionListDto;
import org.gentar.biology.project.search.Search;
import org.gentar.biology.project.search.SearchResult;
import org.gentar.biology.project.search.Searcher;
import org.gentar.biology.project.search.filter.FilterTypes;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * DownloadService.
 */
@Service
public class ProjectSearchDownloadServiceImpl implements ProjectSearchDownloadService {


    private final Environment env;
    ProjectRepository projectRepository;
    private final ProjectValidator projectValidator;
    private final OrthologService orthologService;
    private final Searcher searcher;
    private static final String SEPARATOR = "\",\"";

    public ProjectSearchDownloadServiceImpl(Environment env,
                                            ProjectRepository projectRepository,
                                            ProjectValidator projectValidator,
                                            OrthologService orthologService,
                                            Searcher searcher) {
        this.env = env;
        this.projectRepository = projectRepository;
        this.projectValidator = projectValidator;
        this.orthologService = orthologService;
        this.searcher = searcher;
    }

    @Override
    public void writeReport(HttpServletResponse response, ProjectFilter projectFilter)
        throws IOException {


        ProjectSearchDownloadProjectionListDto filterString = generateFilterString(projectFilter);

        List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos =
            projectRepository.findAllProjectsForCsvFile(filterString).stream()
                .filter(p -> p.getMgi() != null).map(
                ProjectSearchDownloadMapper::projectProjectionToDto).collect(Collectors.toList());

        addSynonyms(projectFilter, filterString, projectSearchDownloadProjectionDtos);

        projectSearchDownloadProjectionDtos.removeIf(this::isRemoveRestrictedObjects);

        addOrthologs(projectSearchDownloadProjectionDtos);

        printReport(response, getSortedByTpnProjectionDtos(projectSearchDownloadProjectionDtos));
    }

    private List<ProjectSearchDownloadProjectionDto> getSortedByTpnProjectionDtos(
        List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos) {

        return projectSearchDownloadProjectionDtos.stream()
            .sorted(
                Comparator.comparing(p -> p.getTpn() != null ? p.getTpn() : p.getBestOrtholog()))
            .collect(
                Collectors.toList());
    }

    private void addSynonyms(ProjectFilter projectFilter,
                             ProjectSearchDownloadProjectionListDto filterString,
                             List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos) {

        List<ProjectSearchDownloadProjectionDto> synonymsDto = new ArrayList<>();

        if (!filterString.getGeneSymbolOrMgi().getFirst().equals("null")) {
            filterString.getGeneSymbolOrMgi().forEach(x -> {
                List<String> filters = new ArrayList<>();
                filters.add(x);
                if (!x.isEmpty() && projectSearchDownloadProjectionDtos.stream()
                    .noneMatch(y -> y.getGeneOrLocation().equals(x)) && !isAccessionId(x)) {
                    Search search =
                        new Search("gene", filters, projectFilter);

                    List<SearchResult> results = new ArrayList<>(searcher.execute(search));
                    for (SearchResult result : results) {
                        if (!isAnyMatch(projectSearchDownloadProjectionDtos, result)) {
                            synonymsDto.add(getDownloadProjectionDto(result));
                        }
                    }
                }
            });

            projectSearchDownloadProjectionDtos.addAll(synonymsDto);
        }
    }

    private boolean isAnyMatch(
        List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos,
        SearchResult result) {
        return projectSearchDownloadProjectionDtos.stream()
            .anyMatch(p -> p.getTpn().equals(getDownloadProjectionDto(result).getTpn()));
    }

    @Override
    public void writeReportCaches() {

        ProjectSearchDownloadProjectionListDto projectSearchDownloadProjectionListDto =
            new ProjectSearchDownloadProjectionListDto(nullList());

        List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos =
            projectRepository.findAllProjectsForCsvFile(projectSearchDownloadProjectionListDto)
                .stream().map(
                ProjectSearchDownloadMapper::projectProjectionToDto).collect(Collectors.toList());

        addOrthologs(projectSearchDownloadProjectionDtos);

    }

    @EventListener(ApplicationReadyEvent.class)
    private void init() {

       if(Arrays.stream(env.getActiveProfiles()).noneMatch(p->p.equals("devgentarschema"))){
        writeReportCaches();
       }

    }


    private Boolean isRemoveRestrictedObjects(
        ProjectSearchDownloadProjectionDto projectSearchDownloadProjectionDto) {
        Project project = new Project();
        Privacy privacy = new Privacy();
        privacy.setName(projectSearchDownloadProjectionDto.getPrivacy() != null ?
            projectSearchDownloadProjectionDto.getPrivacy() : "public");
        project.setPrivacy(privacy);
        return projectValidator.getAccessChecked(project) == null;
    }

    private void printReport(HttpServletResponse response,
                             List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos)
        throws IOException {

        PrintWriter output = response.getWriter();
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("Content-disposition", "attachment; filename=Projects " +
            LocalDate.now() + ".csv");

        output.println(convertListToTsvFileFormat(projectSearchDownloadProjectionDtos));
        output.flush();
        output.close();
    }

    private List<String> convertListToString(
        List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos) {

        List<String> projectProjectionDtoList = new ArrayList<>();
        projectSearchDownloadProjectionDtos.forEach(
            x -> projectProjectionDtoList.add(ProjectSearchDownloadMapper.mapOrthologToString(x)));

        return projectProjectionDtoList;
    }

    private String convertListToTsvFileFormat(
        List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos) {
        String header = generateReportHeaders();

        String report = String.join("\n", convertListToString(projectSearchDownloadProjectionDtos));

        return String.join("\n", header, report);
    }

    private String generateReportHeaders() {
        List<String> headers = Arrays.asList(
            "\"Tpn",
            "Gene/Location",
            "Mgi",
            "Best Human ortholog",
            "Mutation Intentions",
            "Work Unit",
            "Work Group",
            "Project Assignment",
            "Project Summary Status",
            "Production Colony",
            "Phenotyping External Reference",
            "Privacy",
            "Consortia\""

        );

        return String.join(SEPARATOR, headers);

    }


    private List<String> nullList() {
        List<String> nullList = new ArrayList<>();
        nullList.add("null");
        return nullList;
    }


    private void addOrthologs(
        List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos) {


        List<String> mgiIds = new ArrayList<>();
        getMgiIdList(projectSearchDownloadProjectionDtos, mgiIds);

        List<ProjectSearchDownloadOrthologDto> orthologs =
            orthologService.getOrthologs(mgiIds.stream().sorted().collect(
                Collectors.toList()));

        projectSearchDownloadProjectionDtos.forEach(x -> x.setBestOrtholog(
            orthologs.stream().filter(y -> y.getMgiGeneAccId().equals(x.getMgi()))
                .map(ProjectSearchDownloadOrthologDto::getHumanGeneSymbol).findFirst().orElse("")));

    }

    private void getMgiIdList(
        List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos,
        List<String> mgiIds) {
        projectSearchDownloadProjectionDtos
            .forEach(x -> {
                if (!mgiIds.contains(x.getMgi()) && x.getMgi() != null) {
                    mgiIds.add(x.getMgi());
                }
            });
    }

    private boolean isAccessionId(String searchTerm) {
        return searchTerm.contains(":");
    }

    private ProjectSearchDownloadProjectionListDto generateFilterString(
        ProjectFilter searchFilters) {
        ProjectSearchDownloadProjectionListDto projectSearchDownloadProjectionListDto =
            new ProjectSearchDownloadProjectionListDto(nullList());
        for (Map.Entry<FilterTypes, List<String>> searchFilter : searchFilters.getFilters()
            .entrySet()) {
            switch (searchFilter.getKey().getName()) {
                case "tpn"-> projectSearchDownloadProjectionListDto.setTpn(getLowerCaseValue(searchFilter));
                case "intention"-> projectSearchDownloadProjectionListDto.setIntention(getLowerCaseValue(searchFilter));
                case "workUnitName"-> projectSearchDownloadProjectionListDto.setWorkUnit(getLowerCaseValue(searchFilter));
                case "workGroup"-> projectSearchDownloadProjectionListDto.setWorkGroup(getLowerCaseValue(searchFilter));
                case "privacyName"-> projectSearchDownloadProjectionListDto.setPrivacy(getLowerCaseValue(searchFilter));
                case "summaryStatus"-> projectSearchDownloadProjectionListDto.setProjectSummaryStatus(getLowerCaseValue(searchFilter));
                case "consortium"-> projectSearchDownloadProjectionListDto.setConsortia(getLowerCaseValue(searchFilter));
                case "gene"-> projectSearchDownloadProjectionListDto.setGeneSymbolOrMgi(getLowerCaseValue(searchFilter));
                case "phenotypingExternalRefs"-> projectSearchDownloadProjectionListDto.setPhenotypingExternalRef(getLowerCaseValue(searchFilter));
                case "colonyName"-> projectSearchDownloadProjectionListDto.setProductionColonyName(getLowerCaseValue(searchFilter));
            }
        }
        return projectSearchDownloadProjectionListDto;
    }

    private List<String> getLowerCaseValue(Map.Entry<FilterTypes, List<String>> searchFilter) {
        return searchFilter.getValue().stream().map(
            String::toLowerCase).collect(Collectors.toList());
    }

    private ProjectSearchDownloadProjectionDto getDownloadProjectionDto(
        SearchResult result) {
        ProjectSearchDownloadProjectionDto dto =
            new ProjectSearchDownloadProjectionDto();
        if (result.getProject() == null) {
            setEmptyDto(result, dto);
            return dto;
        }

        dto.setTpn(result.getProject().getTpn());
        dto.setGeneOrLocation(
            result.getProject().getProjectIntentions().getFirst()
                .getProjectIntentionGene().getGene().getSymbol());
        dto.setMgi(
            result.getProject().getProjectIntentions().getFirst()
                .getProjectIntentionGene().getGene().getAccId());
        dto.setMutationIntentions(
            result.getProject().getProjectIntentions().getFirst()
                .getMolecularMutationType().getName());
        dto.setWorkUnit(
            result.getProject().getPlans().iterator().next()
                .getWorkUnit()
                .getName());
        dto.setWorkGroup(
            result.getProject().getPlans().iterator().next()
                .getWorkGroup()
                .getName());
        dto.setProjectAssignment(
            result.getProject().getAssignmentStatus().getName());
        dto.setProjectSummaryStatus(
            result.getProject().getSummaryStatus().getName());
        dto.setPrivacy(result.getProject().getPrivacy().getName());
        dto.setConsortia(
            result.getProject().getProjectConsortia().iterator().next()
                .getConsortium().getName());

        return dto;
    }

    private void setEmptyDto(SearchResult result, ProjectSearchDownloadProjectionDto dto) {
        if (!result.getInput().contains(":")) {
            dto.setGeneOrLocation(result.getInput().substring(0, 1).toUpperCase() + result.getInput().substring(1));
        } else {
            dto.setMgi(result.getInput().substring(0, 1).toUpperCase() + result.getInput().substring(1));
        }
        if(result.getComment()==null) {
            dto.setTpn("No Project Found");
        }else{
            dto.setTpn(result.getComment());
        }
    }
}
