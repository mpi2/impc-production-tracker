package org.gentar.biology.ortholog;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

import java.util.List;
import java.util.Map;
import org.gentar.report.ReportServiceImpl;
import org.gentar.report.collection.gene_interest.GeneInterestReportMergeHelperImpl;
import org.gentar.report.collection.gene_interest.phenotyping.GeneInterestReportPhenotyping;
import org.gentar.report.collection.gene_interest.production_type.GeneInterestReportCrisprProduction;
import org.gentar.report.collection.gene_interest.production_type.GeneInterestReportEsCellProduction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JSONToOrthologsMapperTest {


    JSONToOrthologsMapper testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new JSONToOrthologsMapper();
    }

    @Test
    void toOrthologs() {

                String orthologJson =("{\"query0\":[{\"orthologs\":[{\"human_gene\":{\"symbol\":\"OTOGL\",\"hgnc_acc_id\":\"HGNC:26901\"},\"category\":\"LOW\",\"support\":\"OrthoMCL\",\"support_count\":1},{\"human_gene\":{\"symbol\":\"OTOG\",\"hgnc_acc_id\":\"HGNC:8516\"},\"category\":\"GOOD\",\"support\":\"HGNC,OMA,EggNOG,HomoloGene,OrthoMCL,Panther,NCBI,OrthoDB,Ensembl,Inparanoid\",\"support_count\":10}],\"mgi_gene_acc_id\":\"MGI:1202064\",\"symbol\":\"Otog\"}]}");

        Map<String, List<Ortholog>> orthologs = testInstance.toOrthologs(orthologJson);
        assertEquals(orthologs.size(), 1);
    }

}