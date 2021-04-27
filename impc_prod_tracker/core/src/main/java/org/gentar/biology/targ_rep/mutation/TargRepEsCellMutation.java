package org.gentar.biology.targ_rep.mutation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.targ_rep.allele.genebank_file.TargRepGenebankFile;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.mutation.type.TargRepEsCellMutationType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepEsCellMutation extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepEsCellMutationSeq", sequenceName = "TARG_REP_ES_CELL_MUTATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepEsCellMutationSeq")
    private Long id;

    @ToString.Exclude
    @NotNull
    @ManyToOne(targetEntity= TargRepEsCell.class)
    private TargRepEsCell esCell;

    @Column(columnDefinition = "boolean default true")
    private Boolean alleleConfirmed;

    @Column(columnDefinition = "boolean default true")
    private Boolean mgiAlleleSymbolWithoutImpcAbbreviation;

    private String mgiAlleleSymbolSuperscript;

    private String alleleSymbolSuperscriptTemplate;

    private String mgiAlleleAccessionId;

    @ToString.Exclude
    @NotNull
    @ManyToOne(targetEntity= TargRepEsCellMutationType.class)
    private TargRepEsCellMutationType alleleType;

    @ToString.Exclude
    @ManyToOne(targetEntity= TargRepGenebankFile.class)
    private TargRepGenebankFile genebankFile;

    @Column(columnDefinition = "boolean default true")
    private Boolean containsLacZ;
}
