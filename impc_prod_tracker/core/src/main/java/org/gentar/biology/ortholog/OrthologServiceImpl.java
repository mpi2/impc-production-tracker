package org.gentar.biology.ortholog;

import org.apache.commons.collections.CollectionUtils;
import org.gentar.graphql.GraphQLConsumer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrthologServiceImpl implements OrthologService
{
    private final GraphQLConsumer graphQLConsumer;
    private final JSONToOrthologsMapper jsonToOrthologsMapper;

    private static final int THRESHOLD_SUPPORT_COUNT = 4;

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

    public OrthologServiceImpl(GraphQLConsumer graphQLConsumer, JSONToOrthologsMapper jsonToOrthologsMapper)
    {
        this.graphQLConsumer = graphQLConsumer;
        this.jsonToOrthologsMapper = jsonToOrthologsMapper;
    }

    @Override
    @Cacheable("orthologsByIds")
    public Map<String, List<Ortholog>> getOrthologsByAccIds(List<String> accIds)
    {
        Map<String, List<Ortholog>> orthologs = new HashMap<>();
        if (accIds != null && !accIds.isEmpty())
        {
            String query = buildQuery(accIds);
            String result = graphQLConsumer.executeQuery(query);
            orthologs = jsonToOrthologsMapper.toOrthologs(result);
        }
        return orthologs;
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

    public List<Ortholog> calculateBestOrthologs(List<Ortholog> orthologs)
    {
        List<Ortholog> bestOrthologs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orthologs))
        {
            Map<Integer, List<Ortholog>> mappedBySupportCount = new HashMap<>();
            orthologs.forEach(x -> {
                List<Ortholog> elementsWithSameCount = mappedBySupportCount.get(x.getSupportCount());
                if (elementsWithSameCount == null)
                {
                    elementsWithSameCount = new ArrayList<>();
                }
                elementsWithSameCount.add(x);
                mappedBySupportCount.put(x.getSupportCount(), elementsWithSameCount);
            });
            Set<Integer> keys = mappedBySupportCount.keySet();
            Integer max = Collections.max(keys);
            if (max > THRESHOLD_SUPPORT_COUNT)
            {
                bestOrthologs =  mappedBySupportCount.get(max);
            }
        }
        return bestOrthologs;
    }
}
