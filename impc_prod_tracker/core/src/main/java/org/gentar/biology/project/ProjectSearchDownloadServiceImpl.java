package org.gentar.biology.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * DownloadService.
 */
@Service
public class ProjectSearchDownloadServiceImpl implements ProjectSearchDownloadService {

    ProjectRepository projectRepository;
    private final ProjectValidator projectValidator;
    private final OrthologService orthologService;
    private final Searcher searcher;
    private static final String SEPARATOR = ";";

    public ProjectSearchDownloadServiceImpl(ProjectRepository projectRepository,
                                            ProjectValidator projectValidator,
                                            OrthologService orthologService,
                                            Searcher searcher) {
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
            projectRepository.findAllProjectsForCsvFile(filterString).stream().map(
                ProjectSearchDownloadMapper::projectProjectionToDto).collect(Collectors.toList());

        addSynonyms(projectFilter, filterString, projectSearchDownloadProjectionDtos);

        projectSearchDownloadProjectionDtos.removeIf(this::isRemoveRestrictedObjects);

        addOrthologs(projectSearchDownloadProjectionDtos);

        printReport(response, projectSearchDownloadProjectionDtos);
    }

    private void addSynonyms(ProjectFilter projectFilter,
                             ProjectSearchDownloadProjectionListDto filterString,
                             List<ProjectSearchDownloadProjectionDto> projectSearchDownloadProjectionDtos) {

        List<ProjectSearchDownloadProjectionDto> synonymsDto = new ArrayList<>();

            if (!filterString.getGeneSymbolOrMgi().get(0).equals("null")) {
                filterString.getGeneSymbolOrMgi().forEach(x -> {
                    List<String> filters = new ArrayList<>();
                    filters.add(x);
                    if (!x.equals("") && projectSearchDownloadProjectionDtos.stream()
                        .noneMatch(y -> y.getGeneOrLocation().equals(x)) && !isAccessionId(x)) {
                        Search search =
                            new Search("gene", filters, projectFilter);

                        List<SearchResult> result = new ArrayList<>();
                        result.addAll(searcher.execute(search));
                        if (result.get(0).getProject() != null) {
                            synonymsDto.add(getDownloadProjectionDto(result));
                        }
                    }
                });

                projectSearchDownloadProjectionDtos.addAll(synonymsDto);
            }
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
        writeReportCaches();
    }


    private Boolean isRemoveRestrictedObjects(
        ProjectSearchDownloadProjectionDto projectSearchDownloadProjectionDto) {
        Project project = new Project();
        Privacy privacy = new Privacy();
        privacy.setName(projectSearchDownloadProjectionDto.getPrivacy());
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
            "Tpn",
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
            "Consortia"

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
                case "tpn":
                    projectSearchDownloadProjectionListDto.setTpn(searchFilter.getValue());
                    break;
                case "intention":
                    projectSearchDownloadProjectionListDto.setIntention(searchFilter.getValue());
                    break;
                case "workUnitName":
                    projectSearchDownloadProjectionListDto.setWorkUnit(searchFilter.getValue());
                    break;
                case "workGroup":
                    projectSearchDownloadProjectionListDto.setWorkGroup(searchFilter.getValue());
                    break;
                case "privacyName":
                    projectSearchDownloadProjectionListDto.setPrivacy(searchFilter.getValue());
                    break;
                case "summaryStatus":
                    projectSearchDownloadProjectionListDto
                        .setProjectSummaryStatus(searchFilter.getValue());
                    break;
                case "consortium":
                    projectSearchDownloadProjectionListDto.setConsortia(searchFilter.getValue());
                    break;
                case "gene":
                    projectSearchDownloadProjectionListDto
                        .setGeneSymbolOrMgi(searchFilter.getValue());
                    break;
                case "phenotypingExternalRefs":
                    projectSearchDownloadProjectionListDto
                        .setPhenotypingExternalRef(searchFilter.getValue());
                    break;
                case "colonyName":
                    projectSearchDownloadProjectionListDto
                        .setProductionColonyName(searchFilter.getValue());
                    break;
                default:
                    break;
            }
        }
        return projectSearchDownloadProjectionListDto;
    }

    private ProjectSearchDownloadProjectionDto getDownloadProjectionDto(
        List<SearchResult> result) {
        ProjectSearchDownloadProjectionDto dto =
            new ProjectSearchDownloadProjectionDto();
        dto.setTpn(result.get(0).getProject().getTpn());
        dto.setGeneOrLocation(
            result.get(0).getProject().getProjectIntentions().get(0)
                .getProjectIntentionGene().getGene().getSymbol());
        dto.setMgi(
            result.get(0).getProject().getProjectIntentions().get(0)
                .getProjectIntentionGene().getGene().getAccId());
        dto.setMutationIntentions(
            result.get(0).getProject().getProjectIntentions().get(0)
                .getMolecularMutationType().getName());
        dto.setWorkUnit(
            result.get(0).getProject().getPlans().iterator().next()
                .getWorkUnit()
                .getName());
        dto.setWorkGroup(
            result.get(0).getProject().getPlans().iterator().next()
                .getWorkGroup()
                .getName());
        dto.setProjectAssignment(
            result.get(0).getProject().getAssignmentStatus().getName());
        dto.setProjectSummaryStatus(
            result.get(0).getProject().getSummaryStatus().getName());
        dto.setPrivacy(result.get(0).getProject().getPrivacy().getName());
        dto.setConsortia(
            result.get(0).getProject().getProjectConsortia().iterator().next()
                .getConsortium().getName());
        return dto;
    }
}
