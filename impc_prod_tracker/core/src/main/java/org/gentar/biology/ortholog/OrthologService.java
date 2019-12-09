package org.gentar.biology.ortholog;

import org.gentar.graphql.GraphQLConsumer;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrthologService
{
    private GraphQLConsumer graphQLConsumer;
    private JSONToOrthologsMapper jsonToOrthologsMapper;

    static final String ORTHOLOGS_BY_ACC_ID_QUERY =
        "{ \"query\":" +
            " \"{ " +
            "%s" +
            "}\" " +
            "}";
    static final String ORTHOLOGS_BODY_QUERY =
        " %s: mouse_gene(where:" +
            " { mgi_gene_acc_id: {_eq: \\\"%s\\\"}}) {" +
            "   orthologs {" +
            "     human_gene {" +
            "       symbol" +
            "       hgnc_acc_id" +
            "     }" +
            "     category" +
            "     support" +
            "     support_count" +
            "   }" +
            "   mgi_gene_acc_id" +
            "   symbol" +
            " }";

    public OrthologService(
        GraphQLConsumer graphQLConsumer, JSONToOrthologsMapper jsonToOrthologsMapper)
    {
        this.graphQLConsumer = graphQLConsumer;
        this.jsonToOrthologsMapper = jsonToOrthologsMapper;
    }

    public Map<String, List<Ortholog>> getOrthologsByAccIds(List<String> accIds)
    {
        Map<String, List<Ortholog>> orthologs = new HashMap<>();
        accIds.forEach(x ->
        {
            orthologs.put(x, null);
        });
        System.out.println(buildQuery(accIds));
        String query = buildQuery(accIds);
        String result = graphQLConsumer.executeQuery(query);
        System.out.println(result);
        return jsonToOrthologsMapper.toOrthologs(result);
    }

    private String buildQuery(List<String> accIds)
    {
        String query = "";
        AtomicInteger counter = new AtomicInteger();
        StringBuilder builder = new StringBuilder();
        accIds.forEach(x -> {
            String subQueryName = "query" + counter.getAndIncrement();
            builder.append(String.format(ORTHOLOGS_BODY_QUERY, subQueryName, x));
        });
        query = String.format(ORTHOLOGS_BY_ACC_ID_QUERY, builder.toString());
        return query;
    }
}
