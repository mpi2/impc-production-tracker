package uk.ac.ebi.impc_prod_tracker.common.diff;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.PlanRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TempTest
{
    @Autowired
    private PlanRepository planRepository;

    @Test
    public void test()
    {
        Plan plan = planRepository.findPlanByPin("PIN:0000000034");
        ObjectInspector objectInspector =  new ObjectInspector(plan, Arrays.asList("lastModified", "lastModifiedBy", "createdAt", "createdBy", "id"));
        System.out.println("****");
        objectInspector.printSimple();
    }
}
