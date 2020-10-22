package org.gentar.biology.gene_list;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.external_ref.GeneExternalService;
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
    private final GeneExternalService geneExternalService;
    private final ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder;

    public GeneListCsvRecordMapper(
        GeneExternalService geneExternalService,
        ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder)
    {
        this.geneExternalService = geneExternalService;
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
            genes.forEach(x -> {
                Gene gene =
                    geneExternalService.getGeneFromExternalDataBySymbolOrAccId(x.getAccId());
                if (gene == null)
                {
                    genesSymbols.add("gene not found ("+ x.getAccId()+")");
                }
                else
                {
                    genesSymbols.add(gene.getSymbol());
                }
            } );
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
