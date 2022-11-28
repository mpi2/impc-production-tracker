package org.gentar.biology.ortholog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.gentar.biology.project.projection.dto.ProjectSearchDownloadOrthologDto;
import org.gentar.graphql.GraphQLConsumer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrthologServiceImpl implements OrthologService {

    public static final String LOCALHOST_ORTHOLOG_API_URL =
        "http://localhost:8090/api/ortholog/find_one_to_many_by_mgi_ids?mgiIds=";

    public static final String GENTAR_ORTHOLOG_API_URL =
        "https://www.gentar.org/orthology-api/api/ortholog/find_all_by_mgi_ids?mgiIds=";

    public static final String ORTHOLOG_API_URL =
        "http://api-ortholog-service-reference-db.mi-reference-data.svc.cluster.local:8080/orthology-api/api/ortholog/find_all_by_mgi_ids?mgiIds=";

    private static final String HGNC_URL =
        "https://www.genenames.org/data/gene-symbol-report/#!/hgnc_id/";

    public final int CHUNK_SIZE = 200;
    private final GraphQLConsumer graphQLConsumer;
    private final JSONToOrthologsMapper jsonToOrthologsMapper;
    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    private static final int THRESHOLD_SUPPORT_COUNT = 4;

    static final String ORTHOLOGS_BY_ACC_ID_QUERY =
        "{ \"query\":" +
            " \"{ " +
            "%s" +
            "}\" " +
            "}";
    static final String ORTHOLOGS_BODY_QUERY =
        " %s: mouse_gene(where:" +
            " { mgi_gene_acc_id: {_eq: \\\"%s\\\"}}) {" +
            "   orthologs {" +
            "     human_gene {" +
            "       symbol" +
            "       hgnc_acc_id" +
            "     }" +
            "     category" +
            "     support" +
            "     support_count" +
            "   }" +
            "   mgi_gene_acc_id" +
            "   symbol" +
            " }";

    public OrthologServiceImpl(GraphQLConsumer graphQLConsumer,
                               JSONToOrthologsMapper jsonToOrthologsMapper) {
        this.graphQLConsumer = graphQLConsumer;
        this.jsonToOrthologsMapper = jsonToOrthologsMapper;
    }

    @Override
    public Map<String, List<Ortholog>> getOrthologsByAccIds(List<String> mgiIds) {

        List<ProjectSearchDownloadOrthologDto> cachedOrthologs = getOrthologs(mgiIds);

        return mapDtoToOrtholog(cachedOrthologs);

    }

    @Override
    public Map<String, List<Ortholog>> formatOrthologs(
        List<ProjectSearchDownloadOrthologDto> projectSearchDownloadOrthologDto) {
        return mapDtoToOrtholog(projectSearchDownloadOrthologDto);
    }

    public List<Ortholog> calculateBestOrthologs(List<Ortholog> orthologs) {
        List<Ortholog> bestOrthologs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orthologs)) {
            Map<Integer, List<Ortholog>> mappedBySupportCount = new HashMap<>();
            orthologs.forEach(x -> {
                List<Ortholog> elementsWithSameCount =
                    mappedBySupportCount.get(x.getSupportCount());
                if (elementsWithSameCount == null) {
                    elementsWithSameCount = new ArrayList<>();
                }
                elementsWithSameCount.add(x);
                mappedBySupportCount.put(x.getSupportCount(), elementsWithSameCount);
            });
            Set<Integer> keys = mappedBySupportCount.keySet();
            Integer max = Collections.max(keys);
            if (max > THRESHOLD_SUPPORT_COUNT) {
                bestOrthologs = mappedBySupportCount.get(max);
            }
        }
        return bestOrthologs;
    }

    @Override
    @Cacheable("mgiIds")
    public List<ProjectSearchDownloadOrthologDto> getOrthologs(List<String> mgiIds) {

        LOGGER.info("ortholog caching started, number of MGI=" + mgiIds.size());

        final Collection<List<String>> mgiChunks = groupMgiIdsToChunks(mgiIds);

        List<ProjectSearchDownloadOrthologDto> downloadOrthologDtos =
            new ArrayList<>();

        mgiChunks.forEach(mgiChunk -> {

            final String harlowOrthologUri =
                GENTAR_ORTHOLOG_API_URL +
                    String.join(",", mgiChunk);

            ResponseEntity<ProjectSearchDownloadOrthologDto[]> response =
                new RestTemplate().getForEntity(
                    harlowOrthologUri,
                    ProjectSearchDownloadOrthologDto[].class);

            downloadOrthologDtos.addAll(Arrays.asList(
                Objects.requireNonNull(response.getBody())));
        });

        List<ProjectSearchDownloadOrthologDto>
            calculatedOrthologDtos = calculateBestSearchDownloadOrthologs(downloadOrthologDtos);

        List<ProjectSearchDownloadOrthologDto>
            sortedDownloadOrthologDtos = sortDownloadOrthologDtos(calculatedOrthologDtos);

        LOGGER.info("ortholog caching ended, cached ortholog=" + sortedDownloadOrthologDtos.size());
        return sortedDownloadOrthologDtos;

    }

    private List<ProjectSearchDownloadOrthologDto> sortDownloadOrthologDtos(
        List<ProjectSearchDownloadOrthologDto> downloadOrthologDtos) {
        List<ProjectSearchDownloadOrthologDto> sortedDownloadOrthologDtos =
            downloadOrthologDtos.stream()
                .sorted(Comparator.comparing(ProjectSearchDownloadOrthologDto::getMgiGeneAccId))
                .collect(
                    Collectors.toList());
        return sortedDownloadOrthologDtos;
    }

    private Collection<List<String>> groupMgiIdsToChunks(List<String> mgiIds) {

        final AtomicInteger counter = new AtomicInteger();

        return mgiIds.stream()
            .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / CHUNK_SIZE))
            .values();
    }


    public List<ProjectSearchDownloadOrthologDto> calculateBestSearchDownloadOrthologs(
        List<ProjectSearchDownloadOrthologDto> orthologs) {

        List<ProjectSearchDownloadOrthologDto> bestOrthologs = new ArrayList<>(orthologs);

        orthologs.forEach(x -> {
            if (bestOrthologs.stream().filter(y -> y.getMgiGeneAccId().equals(x.getMgiGeneAccId()))
                .count() > 1) {
                ProjectSearchDownloadOrthologDto bestOrtholog =
                    getBestOrthologs(bestOrthologs, x);

                List<ProjectSearchDownloadOrthologDto>
                    bestOrthologSameSupportCount =
                    getBestOrthologWithSameSupportCount(bestOrthologs, x, bestOrtholog);

                removeDuplicatedOrthologs(bestOrthologs, x);

                addDuplicatedOrthologs(bestOrthologs, bestOrtholog, bestOrthologSameSupportCount);
            }

        });
        return bestOrthologs;
    }

    private void addDuplicatedOrthologs(List<ProjectSearchDownloadOrthologDto> bestOrthologs,
                                        ProjectSearchDownloadOrthologDto bestOrtholog,
                                        List<ProjectSearchDownloadOrthologDto> bestOrthologSameSupportCount) {
        if (bestOrthologSameSupportCount.size() > 1) {
            String humanGenSymbol = bestOrthologSameSupportCount.stream().map(
                ProjectSearchDownloadOrthologDto::getHumanGeneSymbol).collect(
                Collectors.joining(":"));
            bestOrtholog.setHumanGeneSymbol(humanGenSymbol);
            bestOrthologs.add(bestOrtholog);
        } else {
            bestOrthologs.add(bestOrtholog);
        }
    }

    private void removeDuplicatedOrthologs(List<ProjectSearchDownloadOrthologDto> bestOrthologs,
                                           ProjectSearchDownloadOrthologDto x) {
        bestOrthologs.removeAll(bestOrthologs.stream().filter(
            y -> y.getMgiGeneAccId().equals(x.getMgiGeneAccId())).collect(
            Collectors.toList()));
    }

    private List<ProjectSearchDownloadOrthologDto> getBestOrthologWithSameSupportCount(
        List<ProjectSearchDownloadOrthologDto> bestOrthologs, ProjectSearchDownloadOrthologDto x,
        ProjectSearchDownloadOrthologDto bestOrtholog) {
        return bestOrthologs.stream().filter(
            y -> y.getMgiGeneAccId().equals(x.getMgiGeneAccId()) &&
                bestOrtholog.getSupportCount().equals(y.getSupportCount())).collect(
            Collectors.toList());
    }

    private ProjectSearchDownloadOrthologDto getBestOrthologs(
        List<ProjectSearchDownloadOrthologDto> bestOrthologs, ProjectSearchDownloadOrthologDto x) {
        ProjectSearchDownloadOrthologDto bestOrtholog = bestOrthologs.stream()
            .filter(y -> y.getMgiGeneAccId().equals(x.getMgiGeneAccId())).collect(
                Collectors.toList()).stream()
            .max(Comparator.comparing(ProjectSearchDownloadOrthologDto::getSupportCount))
            .orElseThrow(NoSuchElementException::new);
        return bestOrtholog;
    }


    private Map<String, List<Ortholog>> mapDtoToOrtholog(
        List<ProjectSearchDownloadOrthologDto> projectSearchDownloadOrthologDtos) {

        Map<String, List<Ortholog>> orthologMaps = new HashMap<>();

        for (ProjectSearchDownloadOrthologDto projectSearchDownloadOrthologDto : projectSearchDownloadOrthologDtos) {

            List<String> bestOrthologs =
                Arrays.asList(projectSearchDownloadOrthologDto.getHumanGeneSymbol().split(":"));
            List<Ortholog> orthologs = new ArrayList<>();
            for (String bestOrtholog : bestOrthologs) {
                Ortholog ortholog = new Ortholog();
                ortholog.setSymbol(bestOrtholog);
                ortholog
                    .setSupportCount(
                        projectSearchDownloadOrthologDto.getSupportCount() == null ? 0 :
                            projectSearchDownloadOrthologDto.getSupportCount().intValue());
                ortholog.setCategory(projectSearchDownloadOrthologDto.getCategory());
                ortholog.setLink(HGNC_URL + bestOrtholog);
                orthologs.add(ortholog);
            }
            orthologMaps
                .put(projectSearchDownloadOrthologDto.getMgiGeneAccId(), orthologs);
        }

        return orthologMaps;
    }
}
