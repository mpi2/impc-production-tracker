package org.gentar.biology.gene_list;

import org.gentar.Mapper;
import org.gentar.biology.gene.GeneService;
import org.gentar.biology.gene_list.record.GeneByListRecord;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.biology.project.Project;
import org.gentar.helpers.GeneListCsvRecord;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class GeneListCsvRecordMapper implements Mapper<ListRecord, GeneListCsvRecord>
{
    private static final String SEPARATOR = ",";
    private final GeneService geneService;
    private final ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder;

    public GeneListCsvRecordMapper(
        GeneService geneService, ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder)
    {
        this.geneService = geneService;
        this.projectsByGroupOfGenesFinder = projectsByGroupOfGenesFinder;
    }

    @Override
    public GeneListCsvRecord toDto(ListRecord listRecord)
    {
        GeneListCsvRecord geneListCsvRecord = new GeneListCsvRecord();
        geneListCsvRecord.setGenes(String.join(SEPARATOR, getGenesSymbols(listRecord)));
        geneListCsvRecord.setNote(listRecord.getNote());
        geneListCsvRecord.setProjectInformation(String.join(SEPARATOR, getProjectsInfo(listRecord)));
        return geneListCsvRecord;
    }

    private List<String> getGenesSymbols(ListRecord listRecord)
    {
        List<String> genesSymbols = new ArrayList<>();
        Set<GeneByListRecord> genes = listRecord.getGenesByRecord();
        if (genes != null)
        {
            genes.forEach(x -> genesSymbols.add(
                geneService.getGeneByAccessionId(x.getAccId()).getSymbol()));
        }
        return genesSymbols;
    }

    private List<String> getProjectsInfo(ListRecord listRecord)
    {
        List<String> projectsInfo = new ArrayList<>();
        Set<GeneByListRecord> genes = listRecord.getGenesByRecord();
        if (genes != null)
        {
            List<Project> projects = projectsByGroupOfGenesFinder.findProjectsByGenes(genes);
            if (projects != null)
            {
                projects.forEach(x -> {
                    String projectInfo = x.getTpn() + "(" + x.getSummaryStatus().getName() + ")";
                    projectsInfo.add(projectInfo);
                });
            }
        }
        return projectsInfo;
    }
}
