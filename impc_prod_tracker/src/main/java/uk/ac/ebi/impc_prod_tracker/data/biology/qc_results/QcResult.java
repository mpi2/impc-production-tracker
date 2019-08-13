package uk.ac.ebi.impc_prod_tracker.data.biology.qc_results;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele.Allele;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class QcResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable=false)
    private Long id;

    @OneToOne(targetEntity = Allele.class)
    private Allele allele;

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

