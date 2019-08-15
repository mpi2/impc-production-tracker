package uk.ac.ebi.impc_prod_tracker.data.biology.genbank_file;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.ac.ebi.impc_prod_tracker.data.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Getter
@Setter
@Entity
public class GenbankFile extends BaseEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String file_gb;
}
