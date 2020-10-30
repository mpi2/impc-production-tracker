package org.gentar.graphql;

import org.gentar.exceptions.UserOperationFailedException;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder
{
    private static final String TEMPLATE =
        "{ \"query\":" +
        "\"{" +
        "  [ROOT](where: {[CONDITIONS]}) { " +
        "  [FIELDS] " +
        "  } " +
        " }\" " +
        "}";
    private String query;
    private String root;
    private List<String> conditions = new ArrayList<>();
    private List<String> fields = new ArrayList<>();

    public static QueryBuilder getInstance()
    {
        return new QueryBuilder();
    }

    public String build()
    {
        validateQuery();
        query = TEMPLATE.replace("[ROOT]", root);
        query = query.replace("[CONDITIONS]", String.join(", ", conditions));
        query = query.replace("[FIELDS]", String.join(" ", fields));
        return query;
    }

    private void validateQuery()
    {
        if (root == null || root.isEmpty())
        {
            throw new UserOperationFailedException("A root need to be defined for the query.");
        }
    }

    public QueryBuilder withRoot(String root)
    {
        this.root = root;
        return this;
    }

    public QueryBuilder withColumnInLikeValuesIgnoreCase(String column, List<String> values)
    {
        List<String> newCondition = new ArrayList<>();
        if (values != null)
        {
            values.forEach(x -> {
                String ilikeCondition =Conditions.createIlikeCondition(column, x);
                newCondition.add(ilikeCondition);
            });
        }
        this.conditions.add(Conditions.createOrCondition(conditionsToString(newCondition)));
        return this;
    }

    public QueryBuilder withColumnInExactMatch(String column, List<String> values)
    {

        String inCondition = null;
        if (values != null)
        {
            inCondition = Conditions.createInCondition(column, values);
        }
        this.conditions.add(inCondition);
        return this;
    }

    public QueryBuilder withFields(List<String> fields)
    {
        this.fields = fields;
        return this;
    }

    private String conditionsToString(List<String> conditions)
    {
        return String.join(", ", conditions);
    }
}
