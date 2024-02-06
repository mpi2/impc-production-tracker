package org.gentar.report.utils.sequence;

import org.gentar.report.collection.mgi_crispr_allele.sequence.MgiCrisprAlleleReportMutationSequenceProjection;
import org.gentar.report.utils.stringencoding.Formatter;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MgiMutationSequenceFormatHelperImpl implements MgiMutationSequenceFormatHelper {

    @Override
    public String formatMutationSeqeunceData(Set<MgiCrisprAlleleReportMutationSequenceProjection> mutationSequenceProjections) {
        String result = "";

        if (mutationSequenceProjections != null) {
            result = mutationSequenceProjections
                    .stream()
                    .sorted(Comparator.comparing(MgiCrisprAlleleReportMutationSequenceProjection::getIndex))
                    .map(this::formatSeqeunce)
                    .collect(Collectors.joining("**"));
        }

        //result = '"' + result + '"';
        return result;
    }

    private String formatSeqeunce(MgiCrisprAlleleReportMutationSequenceProjection projection) {

        String index = projection.getIndex() == null ? "" : projection.getIndex().toString();
        String sequence = projection.getSequence() == null ? "" :
                                            Formatter.clean(projection.getSequence());
        String sequenceType = projection.getSequenceType() == null ? "" :
                                            Formatter.clean(projection.getSequenceType());
        String sequenceCategory = projection.getSequenceCategory() == null ? "" :
                                            Formatter.clean(projection.getSequenceCategory());

        return  "index::" + index + "||" +
                "mutationSeqeunce::" + sequence + "||" +
                "sequenceType::" + sequenceType + "||" +
                "sequenceCategory::" + sequenceCategory;

    }
}
