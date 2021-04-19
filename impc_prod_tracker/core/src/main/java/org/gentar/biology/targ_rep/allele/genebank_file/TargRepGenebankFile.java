package org.gentar.biology.targ_rep.allele.genebank_file;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepGenebankFile extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepGenebankFileSeq", sequenceName = "TARG_REP_GENEBANK_FILE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepGenebankFileSeq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String fileGb;
}
