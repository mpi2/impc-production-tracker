package org.gentar.biology.targ_rep.allele.genbank_file;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class TargRepGenbankFile extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "targRepGenbankFileSeq", sequenceName = "TARG_REP_GENBANK_FILE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "targRepGenbankFileSeq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String fileGb;
}
