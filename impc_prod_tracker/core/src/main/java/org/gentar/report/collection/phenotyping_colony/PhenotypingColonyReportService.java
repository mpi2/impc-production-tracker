package org.gentar.report.collection.phenotyping_colony;

public interface PhenotypingColonyReportService {

    /**
     * Persists a Phenotyping Colony Report to the database, which is used internally for data releases.
     */
    void generatePhenotypingColonyReport();
}
