package org.gentar.biology.project;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.security.abac.ResourcePrivacy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.gentar.mockdata.MockData.*;
import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {


    Project testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new Project(projectMockData());

    }

    @Test
    void getResourcePrivacyWhenPublic() {
        Privacy privacy = new Privacy();
        privacy.setName("public");
        testInstance.setPrivacy(privacy);
        ResourcePrivacy resourcePrivacy = testInstance.getResourcePrivacy();
        assertEquals(resourcePrivacy, ResourcePrivacy.PUBLIC);
    }

    @Test
    void getResourcePrivacyWhenProtected() {
        Privacy privacy = new Privacy();
        privacy.setName("protected");
        testInstance.setPrivacy(privacy);
        ResourcePrivacy resourcePrivacy = testInstance.getResourcePrivacy();
        assertEquals(resourcePrivacy, ResourcePrivacy.PROTECTED);
    }

    @Test
    void getResourcePrivacyWhenRestricted() {
        Privacy privacy = new Privacy();
        privacy.setName("restricted");
        testInstance.setPrivacy(privacy);
        ResourcePrivacy resourcePrivacy = testInstance.getResourcePrivacy();
        assertEquals(resourcePrivacy, ResourcePrivacy.RESTRICTED);
    }

    @Test
    void getResourcePrivacyWhenNone() {
        Privacy privacy = new Privacy();
        privacy.setName("none");
        testInstance.setPrivacy(privacy);

        SystemOperationFailedException exception = assertThrows(SystemOperationFailedException.class, () -> {
            ResourcePrivacy resourcePrivacy = testInstance.getResourcePrivacy();
        });
        String expectedMessage =  "Invalid privacy";
        String actualMessage = exception.getDebugMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void getRestrictedObject() {

        Project project= testInstance.getRestrictedObject();
        assertEquals(project.getTpn(),TPN_000000001);

    }

    @Test
    void getRelatedProductionPlanWorkUnitsPlanTypeProduction() {
        PlanType planType=new PlanType();
        planType.setName(PlanTypeName.PRODUCTION.getLabel());
        testInstance.getPlans().forEach(p-> p.setPlanType(planType));
        List<WorkUnit> workUnitList=testInstance.getRelatedProductionPlanWorkUnits();
        assertEquals(workUnitList,workUnitListMockData());

    }

    @Test
    void getRelatedWorkGroups() {
        List<WorkGroup> workGroupList=testInstance.getRelatedWorkGroups();
        assertEquals(workGroupList,workGroupListMockData());
    }

    @Test
    void getRelatedConsortia() {

        List<Consortium> consortiumList=testInstance.getRelatedConsortia();
        assertEquals(consortiumList,List.of(consortiumMockData()));
    }

    @Test
    void getRelatedColonies() {

        List<Colony> colonyList=testInstance.getRelatedColonies();
        assertEquals(colonyList,List.of(colonyMockData()));
    }

    @Test
    void testToString() {

        String projectAsString =testInstance.toString();
        assertEquals(projectAsString,"tpn=TPN:000000001,assignmentStatus=null");
    }
}