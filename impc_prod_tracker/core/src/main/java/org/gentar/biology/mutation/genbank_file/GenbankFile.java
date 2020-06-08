package org.gentar.biology.mutation.genbank_file;

import lombok.*;
import org.gentar.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
@Data
@Entity
public class GenbankFile extends BaseEntity
{
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name= "file_gb", columnDefinition = "TEXT")
    private String name;
}
