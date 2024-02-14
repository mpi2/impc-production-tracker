package org.gentar.biology.gene_list;

import org.gentar.Mapper;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene.external_ref.GeneExternalService;
import org.gentar.biology.gene_list.record.GeneByListRecord;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.biology.gene_list.record.ListRecordType;
import org.gentar.biology.project.Project;
import org.gentar.helpers.GeneListCsvRecord;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GeneListCsvRecordMapper implements Mapper<ListRecord, GeneListCsvRecord>
{
    private static final String SEPARATOR = ",";
    private final GeneExternalService geneExternalService;
    private final ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder;

    private static Map<String, String> symbolsByAccIds = null;

    public GeneListCsvRecordMapper(
        GeneExternalService geneExternalService,
        ProjectsByGroupOfGenesFinder projectsByGroupOfGenesFinder)
    {
        this.geneExternalService = geneExternalService;
        this.projectsByGroupOfGenesFinder = projectsByGroupOfGenesFinder;
    }

    public void initMap(List<String> accIds)
    {
        symbolsByAccIds = geneExternalService.getSymbolsByAccessionIdsBulk(accIds);
    }

    @Override
    public GeneListCsvRecord toDto(ListRecord listRecord)
    {
        GeneListCsvRecord geneListCsvRecord = new GeneListCsvRecord();
        geneListCsvRecord.setGenes(String.join(SEPARATOR, getCachedGenesSymbols(listRecord, symbolsByAccIds)));
        geneListCsvRecord.setNote(listRecord.getNote());
        geneListCsvRecord.setVisible(listRecord.getVisible());
        geneListCsvRecord.setProjectInformation(String.join(SEPARATOR, getProjectsInfo(listRecord)));
        geneListCsvRecord.setTypes(String.join(SEPARATOR, getTypes(listRecord)));
        return geneListCsvRecord;
    }

    @Override
    public List<GeneListCsvRecord> toDtos(Collection<ListRecord> listRecords)
    {
        List<GeneListCsvRecord> geneListCsvRecords = new ArrayList<>();
        if (listRecords != null)
        {
            List<String> accIds = getAccessionIdsByListRecords(listRecords);
            Map<String, String> symbolsByAccIds =
                geneExternalService.getSymbolsByAccessionIdsBulk(accIds);
            listRecords.forEach(x -> {
                GeneListCsvRecord geneListCsvRecord = new GeneListCsvRecord();
                geneListCsvRecord.setGenes(String.join(SEPARATOR, getCachedGenesSymbols(x, symbolsByAccIds)));
                geneListCsvRecord.setNote(x.getNote());
                geneListCsvRecord.setVisible(x.getVisible());
                geneListCsvRecord.setProjectInformation(String.join(SEPARATOR, getProjectsInfo(x)));
                geneListCsvRecord.setTypes(String.join(SEPARATOR, getTypes(x)));
                geneListCsvRecords.add(geneListCsvRecord);
            });
        }

        return geneListCsvRecords;
    }

    private List<String> getAccessionIdsByListRecords(Collection<ListRecord> listRecords)
    {
        List<String> accIds = new ArrayList<>();
        if (listRecords != null)
        {
            for (ListRecord listRecord : listRecords)
            {
                Set<GeneByListRecord> geneByListRecords = listRecord.getGenesByRecord();
                if (geneByListRecords != null)
                {
                    geneByListRecords.forEach(x -> accIds.add(x.getAccId()));
                }
            }
        }
        return accIds;
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
                    genesSymbols.add("gene not found (" + x.getAccId() + ")");
                } else
                {
                    genesSymbols.add(gene.getSymbol());
                }
            });
        }
        return genesSymbols;
    }

    private List<String> getCachedGenesSymbols(ListRecord listRecord, Map<String, String> symbolsByAccIds)
    {
        List<String> genesSymbols = new ArrayList<>();
        Set<GeneByListRecord> genes = listRecord.getGenesByRecord();
        if (genes != null)
        {
            genes.forEach(x -> {
                String symbol = symbolsByAccIds.get(x.getAccId());
                genesSymbols.add(Objects.requireNonNullElseGet(symbol, () -> "gene not found (" + x.getAccId() + ")"));
            });
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

    private List<String> getTypes(ListRecord listRecord)
    {
        List<String> typesNames = new ArrayList<>();
        Set<ListRecordType> types = listRecord.getListRecordTypes();
        if (types != null)
        {
            types.forEach(x -> typesNames.add(x.getName()));
        }
        return typesNames;
    }
}
