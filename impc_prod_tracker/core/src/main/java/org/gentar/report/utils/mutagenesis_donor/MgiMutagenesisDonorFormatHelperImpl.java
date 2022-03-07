package org.gentar.report.utils.mutagenesis_donor;

import org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor.MgiCrisprAlleleReportMutagenesisDonorProjection;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
class MgiMutagenesisDonorFormatHelperImpl implements MgiMutagenesisDonorFormatHelper {

    @Override
    public String formatMutagenesisDonorData (Set<MgiCrisprAlleleReportMutagenesisDonorProjection> projections){
        String result = "";

        if (projections != null) {
            result = projections
                    .stream()
                    .map(p -> formatMutagenesisDonor(p))
                    .collect(Collectors.joining("**"));
        }

        return result;
    }

    private String formatMutagenesisDonor(MgiCrisprAlleleReportMutagenesisDonorProjection projection) {

        String sequence = projection.getSequence() == null ? "" : projection.getSequence();
        String vector = projection.getVector() == null ? "" : projection.getVector();
        String preparationType = projection.getPreparationType() == null ? "" : projection.getPreparationType();


        return  '"' +
                "preparationType::" + preparationType + "||" +
                "vector::" + vector + "||" +
                "sequence::" + sequence +
                '"';
    }
}
