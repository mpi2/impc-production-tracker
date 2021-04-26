package org.gentar.biology.targ_rep.mutation;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.gentar.BaseEntity;
import org.gentar.biology.targ_rep.allele.genebank_file.TargRepGenebankFile;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.mutation.type.TargRepAlleleType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepMutation extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepMutationSeq", sequenceName = "TARG_REP_MUTATION_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepMutationSeq")
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
    @ManyToOne(targetEntity= TargRepAlleleType.class)
    private TargRepAlleleType alleleType;

    @ToString.Exclude
    @ManyToOne(targetEntity= TargRepGenebankFile.class)
    private TargRepGenebankFile genebankFile;

    @Column(columnDefinition = "boolean default true")
    private Boolean containsLacZ;
}
