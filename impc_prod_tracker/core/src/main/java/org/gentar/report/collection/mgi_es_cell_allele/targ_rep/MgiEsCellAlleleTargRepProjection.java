package org.gentar.report.collection.mgi_es_cell_allele.targ_rep;

import org.springframework.beans.factory.annotation.Value;

public interface MgiEsCellAlleleTargRepProjection {

    @Value("#{target.mgiAccessionId}")
    String getMgiAccessionId();

    @Value("#{target.assembly}")
    String getAssembly();

    @Value("#{target.cassette ?: ''}")
    String getCassette();

    @Value("#{target.cassetteStart ?: NULL}")
    Long getCassetteStart();

    @Value("#{target.cassetteEnd ?: NULL}")
    Long getCassetteEnd();

    @Value("#{target.loxpStart ?: NULL}")
    Long getLoxpStart();

    @Value("#{target.loxpEnd ?: NULL}")
    Long getLoxpEnd();

    @Value("#{target.pipeline}")
    String getPipeline();

    @Value("#{target.ikmcProjectId ?: NULL }")
    Long getIkmcProjectId();

    @Value("#{target.esCellClone}")
    String getEsCellClone();

    @Value("#{target.parentCellLine}")
    String getParentCellLine();

    @Value("#{target.mutationType}")
    String getMutationType();

    @Value("#{target.mutationSubtype ?: ''}")
    String getMutationSubtype();
}
