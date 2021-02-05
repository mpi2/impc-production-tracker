package org.gentar.report.collection.gene_interest;

public interface GeneInterestReportService {

    /**
     * Persists a Gene Interest Report to the database, which is used to support Register Interest.
     */
    void generateGeneInterestReport();

}
