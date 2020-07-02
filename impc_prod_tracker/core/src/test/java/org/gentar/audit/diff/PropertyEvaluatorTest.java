package org.gentar.audit.diff;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class PropertyEvaluatorTest
{
    private PropertyEvaluator propertyEvaluator;

    @Test
    public void testWithParentDataNull()
    {
        ClassA classA = new ClassA("a", "b");
        PropertyDefinition dataInput = new PropertyDefinition();
        dataInput.setName("p1");
        dataInput.setType(String.class);
        propertyEvaluator = new PropertyEvaluator(dataInput, classA, null);
        propertyEvaluator.evaluate();
        PropertyDescription dataOutput = propertyEvaluator.getData();

        assertThat("map:", dataOutput, is(notNullValue()));
        assertThat("map:", dataOutput.getPropertyDefinition().getName(), is("p1"));
        assertThat("map:", dataOutput.getType(), is(String.class));
        assertThat("map:", dataOutput.getValue(), is(notNullValue()));
        assertThat("map:", dataOutput.isSimpleValue(), is(true));
    }

    @Test
    public void testWithParentDataNotNull()
    {
        ClassA classA = new ClassA("a", "b");

        PropertyDefinition dataInput = new PropertyDefinition();
        dataInput.setName("p1");
        dataInput.setType(String.class);

        propertyEvaluator = new PropertyEvaluator(dataInput, classA, "parentPropertyName");
        propertyEvaluator.evaluate();
        PropertyDescription outputData = propertyEvaluator.getData();

        assertThat("map:", outputData, is(notNullValue()));
        assertThat("map:", outputData.getPropertyDefinition().getName(), is("parentPropertyName.p1"));
        assertThat("map:", outputData.getType(), is(String.class));
        assertThat("map:", outputData.getValue(), is(notNullValue()));
        assertThat("map:", outputData.isSimpleValue(), is(true));
    }

    @Data
    public static class ClassA
    {
        private String p1;
        private String p2;
        private List<String> p3;

        ClassA(String p1, String p2)
        {
            this.p1 = p1;
            this.p2 = p2;
        }
    }
}