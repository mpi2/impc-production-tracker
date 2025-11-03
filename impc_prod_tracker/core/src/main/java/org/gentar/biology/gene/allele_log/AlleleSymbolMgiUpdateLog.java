package org.gentar.biology.gene.allele_log;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class AlleleSymbolMgiUpdateLog extends BaseEntity {

    @Id
    @SequenceGenerator(name = "alleleSymbolMgiUpdateLogSeq", sequenceName = "ALLELE_SYMBOL_MGI_UPDATE_LOG_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AlleleSymbolMgiUpdateLogSeq")
    private Long id;
    private String new_mgi_id;
    private String old_mgi_id;
    private String new_symbol;
    private String old_symbol;
}
