package org.gentar.graphql;

import java.util.ArrayList;
import java.util.List;

public class Conditions
{
    private static final String COLUMN_OPERATOR_STRING_TEMPLATE = "{%s: {%s: \\\"%s\\\"}}";
    private static final String COLUMN_OPERATOR_LIST_TEMPLATE = "%s: {%s: [%s]}";
    private static final String COLUMN_OPERATOR_NUMBER_TEMPLATE = "{%s: {%s: %d}}";

    // Avoid instantiation.
    private Conditions()
    {

    }

    public static String createOrCondition(String conditions)
    {
        String orTemplate = "%s: [%s]";
        return String.format(orTemplate, Operators.OR.getName(), conditions);
    }

    public static String createEqCondition(String column, String value)
    {
        return createColumnOperatorStringCondition(column, Operators.EQ.getName(), value);
    }

    public static String createEqCondition(String column, double value)
    {
        return createColumnOperatorNumberCondition(column, Operators.EQ.getName(), value);
    }

    public static String createNeqCondition(String column, String value)
    {
        return createColumnOperatorStringCondition(column, Operators.NEQ.getName(), value);
    }

    public static String createNeqCondition(String column, double value)
    {
        return createColumnOperatorNumberCondition(column, Operators.NEQ.getName(), value);
    }

    public static String createGtCondition(String column, double value)
    {
        return createColumnOperatorNumberCondition(column, Operators.GT.getName(), value);
    }

    public static String createGteCondition(String column, double value)
    {
        return createColumnOperatorNumberCondition(column, Operators.GTE.getName(), value);
    }

    public static String createLtCondition(String column, double value)
    {
        return createColumnOperatorNumberCondition(column, Operators.LT.getName(), value);
    }

    public static String createLteCondition(String column, double value)
    {
        return createColumnOperatorNumberCondition(column, Operators.LTE.getName(), value);
    }

    public static String createLikeCondition(String column, String value)
    {
        return createColumnOperatorStringCondition(column, Operators.LIKE.getName(), value);
    }

    public static String createNlikeCondition(String column, String value)
    {
        return createColumnOperatorStringCondition(column, Operators.NLIKE.getName(), value);
    }

    public static String createIlikeCondition(String column, String value)
    {
        return createColumnOperatorStringCondition(column, Operators.ILIKE.getName(), value);
    }

    public static String createNilikeCondition(String column, String value)
    {
        return createColumnOperatorStringCondition(column, Operators.NILIKE.getName(), value);
    }

    public static String createInCondition(String column, List<String> values)
    {
        List<String> quotedValues = new ArrayList<>();
        values.forEach(x -> quotedValues.add("\\\"" + x + "\\\""));
        String commaSeparatedValues = String.join(",", quotedValues);
        return createColumnOperatorListCondition(column, Operators.IN.getName(), commaSeparatedValues);
    }

    private static String createColumnOperatorStringCondition(
        String column, String operator, String value)
    {
        return String.format(COLUMN_OPERATOR_STRING_TEMPLATE, column, operator, value);
    }

    private static String createColumnOperatorNumberCondition(
        String column, String operator, double value)
    {
        return String.format(COLUMN_OPERATOR_NUMBER_TEMPLATE, column, operator, value);
    }

    private static String createColumnOperatorListCondition(
        String column, String operator, String commaSeparatedQuotedValues)
    {
        return String.format(COLUMN_OPERATOR_LIST_TEMPLATE, column, operator, commaSeparatedQuotedValues);
    }
}
