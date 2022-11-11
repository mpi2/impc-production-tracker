package org.gentar.report.utils.mutagenesis_donor;

import org.gentar.report.collection.mgi_crispr_allele.mutagenesis_donor.MgiCrisprAlleleReportMutagenesisDonorProjection;
import org.gentar.report.utils.stringencoding.Formatter;
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
        //result = '"' + result + '"';
        return result;
    }

    private String formatMutagenesisDonor(MgiCrisprAlleleReportMutagenesisDonorProjection projection) {

        String sequence = projection.getSequence() == null ? "" :
                                    Formatter.clean(projection.getSequence());
        String vector = projection.getVector() == null ? "" :
                                    Formatter.clean(projection.getVector());
        String preparationType = projection.getPreparationType() == null ? "" :
                                    Formatter.clean(projection.getPreparationType());


        return  "preparationType::" + preparationType + "||" +
                "vector::" + vector + "||" +
                "sequence::" + sequence;
    }
}
