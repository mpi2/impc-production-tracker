package org.gentar.biology.targ_rep.mutation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
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
}
