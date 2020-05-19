package org.gentar.biology.plan;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.util.JsonHelper;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class PlanCommonDataDTOTest
{

    public static final String WORK_UNIT_NAME = "workUnitName";
    public static final String WORK_GROUP_NAME = "workGroupName";
    public static final String FUNDER_NAME = "funderName";
    public static final String COMMENT = "comment";
    public static final boolean PRODUCTS_AVAILABLE_FOR_GENERAL_PUBLIC = true;

    @Test
    public void testStructure() throws JsonProcessingException
    {
        PlanCommonDataDTO planCommonDataDTO = new PlanCommonDataDTO();
        planCommonDataDTO.setWorkUnitName(WORK_UNIT_NAME);
        planCommonDataDTO.setWorkGroupName(WORK_GROUP_NAME);
        planCommonDataDTO.setFunderNames(Arrays.asList(FUNDER_NAME));
        planCommonDataDTO.setComment(COMMENT);
        planCommonDataDTO.setProductsAvailableForGeneralPublic(PRODUCTS_AVAILABLE_FOR_GENERAL_PUBLIC);
        String json = JsonHelper.toJson(planCommonDataDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"funderNames\":[\"funderName\"],\"workUnitName\":\"workUnitName\"," +
            "\"workGroupName\":\"workGroupName\",\"comment\":\"comment\"," +
            "\"productsAvailableForGeneralPublic\":true}"));
    }
}