package org.gentar.biology.genbank_file;

import lombok.*;
import org.gentar.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Data
@Entity
public class GenbankFile extends BaseEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String file_gb;
}
