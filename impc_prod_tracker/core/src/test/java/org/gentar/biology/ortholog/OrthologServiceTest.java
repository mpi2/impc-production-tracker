package org.gentar.biology.ortholog;

import org.gentar.graphql.GraphQLConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
class OrthologServiceTest
{
    @Mock
    private GraphQLConsumer graphQLConsumer;
    @Mock
    private JSONToOrthologsMapper jsonToOrthologsMapper;

    @Autowired
    private OrthologService testInstance = new OrthologServiceImpl(graphQLConsumer, jsonToOrthologsMapper);

    @Test
    public void testCalculateBestOrthologs()
    {
        List<Ortholog> orthologs = new ArrayList<>();
        Ortholog ortholog1 = new Ortholog();
        ortholog1.setSupportCount(10);
        ortholog1.setSymbol("symbol1");

        Ortholog ortholog2 = new Ortholog();
        ortholog2.setSupportCount(10);
        ortholog2.setSymbol("symbol2");

        Ortholog ortholog3 = new Ortholog();
        ortholog3.setSupportCount(4);
        ortholog3.setSymbol("symbol3");

        Ortholog ortholog4 = new Ortholog();
        ortholog4.setSupportCount(3);
        ortholog4.setSymbol("symbol4");

        orthologs.add(ortholog1);
        orthologs.add(ortholog2);
        orthologs.add(ortholog3);
        orthologs.add(ortholog4);

        List<Ortholog> best = testInstance.calculateBestOrthologs(orthologs);
        assertThat("Number of best orthologs:", best.size(), is(2));
        assertThat("Not expected result best ortholog # 1:", best.get(0).getSymbol(), is("symbol1"));
        assertThat("Not expected result best ortholog # 2:", best.get(1).getSymbol(), is("symbol2"));
    }
}