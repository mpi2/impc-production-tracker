package org.gentar.reports.mgi;

import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene_list.record.GeneListProjection;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.MutationGeneProjection;
import org.gentar.biology.mutation.MutationServiceImpl;
import org.gentar.biology.outcome.OutcomeMutationProjection;
import org.gentar.biology.outcome.OutcomeServiceImpl;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptProjectionForMgi;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttemptServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class MgiReportController {

    private final PhenotypingAttemptServiceImpl phenotypingAttemptService;
    private final OutcomeServiceImpl outcomeService;
    private final MutationServiceImpl mutationService;

    public MgiReportController( PhenotypingAttemptServiceImpl phenotypingAttemptServiceImpl,
                                OutcomeServiceImpl outcomeService,
                                MutationServiceImpl mutationService){
        this.phenotypingAttemptService = phenotypingAttemptServiceImpl;
        this.outcomeService = outcomeService;
        this.mutationService = mutationService;
    }

    @GetMapping("/mgi/export")
    public void exportCsv(HttpServletResponse response) throws IOException
    {

        List<PhenotypingAttemptProjectionForMgi> pap = phenotypingAttemptService.getPhenotypingAttemptProjectionsForMgi();
        List<Long> outcomeIds = pap.stream().map(x -> x.getOutcomeId()).collect(Collectors.toList());

        List<OutcomeMutationProjection> omp = outcomeService.getSelectedOutcomeMutationProjections(outcomeIds);
        Map<Long, Set<Mutation>> outcomeMutationMap = omp.stream().collect(Collectors.groupingBy(OutcomeMutationProjection::getOutcomeId, Collectors.mapping(OutcomeMutationProjection::getMutation, Collectors.toSet())));

        // select outcomes associated with only one mutation - compatible with existing MGI iMits report
        List<Long> filteredMutationIds = outcomeMutationMap.entrySet().stream().filter(e -> e.getValue().size() == 1).map(e -> List.copyOf(e.getValue()).get(0).getId()).collect(Collectors.toList());
        Map<Long, Mutation> filteredOutcomeMutationMap = outcomeMutationMap.entrySet().stream().filter(e -> e.getValue().size() == 1).collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));

        // Check filteredOutcomeMutationMap contents
        filteredOutcomeMutationMap.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\t" + e.getValue().getSymbol()));

        List<MutationGeneProjection> mgp = mutationService.getSelectedMutationGeneProjections(filteredMutationIds);
        Map<Long, Set<Gene>> mutationGeneMap = mgp.stream().collect(Collectors.groupingBy(MutationGeneProjection::getMutationId, Collectors.mapping(MutationGeneProjection::getGene, Collectors.toSet())));

        // select mutations associated with only one gene - compatible with existing MGI iMits report
        Map<Long, Gene> filteredMutationGeneMap = mutationGeneMap.entrySet().stream().filter(e -> e.getValue().size() == 1).collect(Collectors.toMap(map -> map.getKey(), map -> List.copyOf(map.getValue()).get(0)));


        pap.stream()
                .filter(x -> filteredOutcomeMutationMap.containsKey(x.getOutcomeId()))
                .filter(x -> filteredMutationGeneMap.containsKey(filteredOutcomeMutationMap.get(x.getOutcomeId()).getId()))
                .forEach(x -> {
                            Mutation m = filteredOutcomeMutationMap.get(x.getOutcomeId());
                            Gene g = filteredMutationGeneMap.get(filteredOutcomeMutationMap.get(x.getOutcomeId()).getId());
                            String cohortProductionWorkUnit = x.getCohortProductionWorkUnit() == null ? x.getPhenotypingWorkUnit() : x.getCohortProductionWorkUnit();
                            System.out.println(
                                    g.getSymbol() + "\t" +
                                            g.getAccId() + "\t" +
                                            x.getColonyName() + "\t" +
                                            "\t" +
                                            x.getStrainName() + "\t" +
                                            x.getStrainAccId() + "\t" +
                                            x.getProductionWorkUnit() + "\t" +
                                            x.getProductionWorkGroup() + "\t" +
                                            x.getPhenotypingWorkUnit() + "\t" +
                                            x.getPhenotypingWorkGroup() + "\t" +
                                            cohortProductionWorkUnit + "\t" +
                                            m.getSymbol()

                            );
                    if (g.getSymbol().equals("Zbtb48")){
                        System.out.println(x);
                        System.out.println(x.getProductionWorkUnit());
                        System.out.println(x.getProductionWorkGroup());
                        System.out.println(x.getPhenotypingWorkUnit());
                        System.out.println(x.getPhenotypingWorkGroup());
                    }

                });

        pap.stream().forEach(x -> {
            System.out.println(
                            x.getColonyName() + "\t" +
                            "\t" +
                            x.getStrainName() + "\t" +
                            x.getStrainAccId() + "\t" +
                            x.getProductionWorkUnit() + "\t" +
                            x.getProductionWorkGroup() + "\t" +
                            x.getPhenotypingWorkUnit() + "\t" +
                            x.getPhenotypingWorkGroup() + "\t" +
                            x.getOutcomeId());

        });
    }
}
