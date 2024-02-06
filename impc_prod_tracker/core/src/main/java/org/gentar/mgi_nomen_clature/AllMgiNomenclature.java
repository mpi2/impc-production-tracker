package org.gentar.mgi_nomen_clature;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gentar.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Data
@Entity
public class AllMgiNomenclature extends BaseEntity {
    @Id
    @SequenceGenerator(name = "allMgiNomenclatureSeq", sequenceName = "ALL_MGI_NOMENCLATURE_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allMgiNomenclatureSeq")
    private Long id;

    String markerMgiId;
    String currentMarkerSymbol;
    String previousMarkerSymbol;
    String event;
    String eventDate;
}
