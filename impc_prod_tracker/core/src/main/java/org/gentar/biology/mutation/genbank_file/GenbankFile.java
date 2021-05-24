package org.gentar.biology.mutation.genbank_file;

import lombok.*;
import org.gentar.BaseEntity;

import javax.persistence.*;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class GenbankFile extends BaseEntity
{
    @Id
    @SequenceGenerator(name = "genbankFileSeq", sequenceName = "GENBANK_FILE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genbankFileSeq")
    private Long id;

    private Long imitsGenbankFile;

    @Column(name= "file_gb", columnDefinition = "TEXT")
    private String name;
}
