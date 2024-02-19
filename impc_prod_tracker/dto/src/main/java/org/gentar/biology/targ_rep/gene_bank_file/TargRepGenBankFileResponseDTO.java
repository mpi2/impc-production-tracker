package org.gentar.biology.targ_rep.gene_bank_file;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "targ_rep_genbank_file")
@Data
@RequiredArgsConstructor
public class TargRepGenBankFileResponseDTO extends
    RepresentationModel<TargRepGenBankFileResponseDTO> {

    private Long id;

    private String host;

    private String type;

    private String genbankFilePath;

    private String alleleImagePath;

    private String cassetteImagePath;

    private String simpleImagePath;

    private String vectorImagePath;


}
