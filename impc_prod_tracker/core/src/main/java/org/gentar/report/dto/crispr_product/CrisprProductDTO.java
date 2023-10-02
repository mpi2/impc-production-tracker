package org.gentar.report.dto.crispr_product;

import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CrisprProductDTO {

    private String mutationId;
    private String mgiAlleleId;
    private String alleleSymbol;
    private String alleleSuperscript;
    private String mgiGeneAccessionId;
    private String planIdentifier;
    private Attempt attempt;
    private String outcomeIdentifier;
    private String colonyName;
    private List<Guide> guides;
    private List<Nuclease> nucleases;
    private List<GenotypePrimer> genotypePrimers;
    private List<Fasta> fasta;
    public List<MutagenesisDonor> mutagenesisDonors;
    public List<Reagent> reagents;
    public Assay assay;

}
