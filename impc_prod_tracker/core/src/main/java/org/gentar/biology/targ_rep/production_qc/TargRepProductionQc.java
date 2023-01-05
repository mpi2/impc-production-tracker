package org.gentar.biology.targ_rep.production_qc;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.gentar.BaseEntity;

@Data
@Entity
public class TargRepProductionQc extends BaseEntity {
    @Id
    @SequenceGenerator(name = "TargRepProductionQcSeq", sequenceName = "TARG_REP_PRODUCTION_QC_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TargRepProductionQcSeq")
    private Long id;
    private Long imitsPcqId;
    private Long imitsAlleleId;
    private Long esCellId;
    private Boolean alleleConfirmed;
    private Boolean mgiAlleleSymbolWithoutImpcAbbreviation;
    private String mgiAlleleSymbolSuperscript;
    private String alleleSymbolSuperscriptTemplate;
    private String mgiAlleleAccessionId;
    private String alleleType;
    private Long genbankFileId;
    private Long imitsColonyId;
    private Long autoAlleleDescription;
    private Long alleleDescription;
    private Long mutantFa;
    private String genbankTransition;
    private Boolean sameAsEsCell;
    private String alleleSubtype;
    private Boolean containsLacz;
    private byte[] bamFile;
    private byte[] bamFileIndex;
    private byte[] vcfFile;
    private byte[] vcfFileIndex;
    private String fivePrimeScreen;
    private String threePrimeScreen;
    private String loxpScreen;
    private String lossOfAllele;
    private String vectorIntegrity;
    private String southernBlot;
    private String fivePrimeLrPcr;
    private String fivePrimeCassetteIntegrity;
    private String tvBackboneAssay;
    private String neoCountQpcr;
    private String laczCountQpcr;
    private String neoSrPcr;
    private String loaQpcr;
    private String homozygousLoaSrPcr;
    private String laczSrPcr;
    private String mutantSpecificSrPcr;
    private String loxpConfirmation;
    private String threePrimeLrPcr;
    private String criticalRegionQpcr;
    private String loxpSrpcr;
    private String loxpSrpcrAndSequencing;


}
