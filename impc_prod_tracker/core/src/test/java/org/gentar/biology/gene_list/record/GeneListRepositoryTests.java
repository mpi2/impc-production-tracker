package org.gentar.biology.gene_list.record;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import org.gentar.biology.gene_list.GeneList;
import org.gentar.biology.gene_list.GeneListRepository;
import org.gentar.organization.consortium.Consortium;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.gentar.projection.db.DBSetupFilePaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.SQLOutput;
import java.util.List;

@DataJpaTest
@ContextConfiguration(classes = {GeneListRepositoryTestConfig.class, RestTemplateAutoConfiguration.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
@DatabaseSetup(DBSetupFilePaths.GENE_LIST_PROJECTION)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = DBSetupFilePaths.GENE_LIST_PROJECTION)
public class GeneListRepositoryTests {

    @Autowired
    private GeneListRepository geneListRepository;


    @Test
    public void shouldFindCMGConsortiumWhenSearchedForByConsortiumNameTest() {
        //given
        Consortium c = new Consortium();

        //when
        c.setName("CMG");
        GeneList geneList = geneListRepository.findByConsortiumName(c.getName());

        // then
        assertThat(geneList.getConsortium().getName().equals(c.getName()), is(Boolean.TRUE));

    }

    @Test
    public void findGeneListProjectionsByConsortiumNameTest() {
        //given
        Consortium c = new Consortium();
        c.setName("CMG");

        //when
        List<GeneListProjection> glp = geneListRepository.findAllGeneListProjectionsByConsortiumName(c.getName());

        //then
        System.out.println(glp.toString());
        assertThat(glp, is(IsNull.notNullValue()));

    }

    @Test
    public void findGeneListProjectionDataByConsortiumNameTest() {
        //given
        Consortium c = new Consortium();
        c.setName("CMG");

        //when
        List<GeneListProjection> glp = geneListRepository.findAllGeneListProjectionsByConsortiumName(c.getName());

        //then
        System.out.println(glp.size());
        System.out.println(glp.getClass());
        System.out.println(glp.get(0).getClass().getCanonicalName());
        System.out.println(glp.get(0).getId());
        System.out.println(glp.get(0).getTpn());
        System.out.println(glp.get(0).getSummaryStatus());
        System.out.println(glp.get(0).getSymbol());
        System.out.println(glp.get(0).getPrivacy());
        assertThat(glp, is(IsNull.notNullValue()));

    }
}
