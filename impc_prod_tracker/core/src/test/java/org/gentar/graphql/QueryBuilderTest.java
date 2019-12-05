package org.gentar.graphql;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class QueryBuilderTest
{
    @Test
    public void test()
    {
        QueryBuilder queryBuilder = QueryBuilder.getInstance();
        String query = queryBuilder
            .withRoot("mouse_gene")
            .withColumnInLikeValuesIgnoreCase("symbol", Arrays.asList("Acc%","Anx%"))
            .withFields(Arrays.asList("mgi_gene_acc_id", "symbol"))
            .build();
        System.out.println(query);
    }
}