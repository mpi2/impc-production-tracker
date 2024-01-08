package org.gentar.biology.gene.external_ref;

/**
 * Constants used by the external reference service
 */
class ExternalReferenceConstants
{
    static final String GENE_BY_SYMBOL_OR_ACC_ID_QUERY =
        "{ \"query\":" +
            " \"{ mouse_gene(where: {" +
            "  _or: [{symbol: {_ilike: \\\"%s\\\"}}, {mgi_gene_acc_id: {_ilike: \\\"%s\\\"}}]" +
            "}) {" +
            "    mgi_gene_acc_id" +
            "    symbol" +
            "    name" +
            "    type" +
            "    genome_build" +
            "    entrez_gene_acc_id" +
            "    ncbi_chromosome" +
            "    ncbi_start" +
            "    ncbi_stop" +
            "    ncbi_strand" +
            "    ensembl_gene_acc_id" +
            "    ensembl_chromosome" +
            "    ensembl_start" +
            "    ensembl_stop" +
            "    ensembl_strand" +
            "    mgi_cm" +
            "    mgi_chromosome" +
            "    mgi_start" +
            "    mgi_stop" +
            "    mgi_strand" +
            "  }" +
            "}\" " +
            "}";

    static final String GENE_BY_SYMBOL_OR_ACC_ID_QUERY_FOR_LESS_THEN_3_CHAR =
            "{ \"query\":" +
                    " \"{ mouse_gene(where: {" +
                    "  _or: [{symbol: { _ilike: \\\"%s\\\" }}, {mgi_gene_acc_id: {_ilike: \\\"%s\\\"}}]" +
                    "}) {" +
                    "    mgi_gene_acc_id" +
                    "    symbol" +
                    "    name" +
                    "    type" +
                    "    genome_build" +
                    "    entrez_gene_acc_id" +
                    "    ncbi_chromosome" +
                    "    ncbi_start" +
                    "    ncbi_stop" +
                    "    ncbi_strand" +
                    "    ensembl_gene_acc_id" +
                    "    ensembl_chromosome" +
                    "    ensembl_start" +
                    "    ensembl_stop" +
                    "    ensembl_strand" +
                    "    mgi_cm" +
                    "    mgi_chromosome" +
                    "    mgi_start" +
                    "    mgi_stop" +
                    "    mgi_strand" +
                    "  }" +
                    "}\" " +
                    "}";

    static final String SYNONYM_BY_SYMBOL_QUERY =
        "{ \"query\":" +
            " \"{" +
            "  mouse_gene(where: {mouse_gene_synonym_relations: {mouse_gene_synonym: {synonym: {_ilike: \\\"%s\\\"}}}}) {" +
            "    symbol" +
            "    mgi_gene_acc_id" +
            "  }" +
            "}\" " +
            "}";
}
