package org.gentar.biology.targ_rep.mutation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.targ_rep.genbank_file.TargRepGenbankFile;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.mutation.type.TargRepEsCellMutationType;


/**
 * TargRepEsCellMutation.
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class TargRepEsCellMutation extends BaseEntity {
    @Id
    @SequenceGenerator(name = "targRepEsCellMutationSeq",
        sequenceName = "TARG_REP_ES_CELL_MUTATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "targRepEsCellMutationSeq")
    private Long id;

    @ToString.Exclude
    @NotNull
    @ManyToOne(targetEntity = TargRepEsCell.class)
    private TargRepEsCell esCell;

    @Column(columnDefinition = "boolean default true")
    private Boolean alleleConfirmed;

    @Column(columnDefinition = "boolean default true")
    private Boolean mgiAlleleSymbolWithoutImpcAbbreviation;

    private String mgiAlleleSymbolSuperscript;

    private String alleleSymbolSuperscriptTemplate;

    private String mgiAlleleAccessionId;

    @ToString.Exclude
    @ManyToOne(targetEntity = TargRepEsCellMutationType.class)
    private TargRepEsCellMutationType alleleType;

    @ToString.Exclude
    @ManyToOne(targetEntity = TargRepGenbankFile.class)
    private TargRepGenbankFile genbankFile;

    @Column(columnDefinition = "boolean default true")
    private Boolean containsLacz;

    private Long imitsPcqId;
    private Long imitsAlleleId;
    private Long imitsColonyId;
    @Column(columnDefinition = "TEXT")
    private String autoAlleleDescription;
    @Column(columnDefinition = "TEXT")
    private String alleleDescription;
    @Column(columnDefinition = "TEXT")
    private String mutantFa;
    private String genbankTransition;
    private Boolean sameAsEsCell;
    private String alleleSubtype;
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
