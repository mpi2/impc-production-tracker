package org.gentar.biology.status_stamps;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gentar.util.JsonHelper;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

class StatusStampsDTOTest
{
    @Test
    public void testStructure() throws JsonProcessingException
    {
        StatusStampsDTO statusStampsDTO = new StatusStampsDTO();
        statusStampsDTO.setStatusName("status name");
        statusStampsDTO.setDate(LocalDateTime.of(2020, 1, 1, 1, 1));
        String json = JsonHelper.toJson(statusStampsDTO);
        assertThat(json, is(notNullValue()));
        assertThat(json, is("{\"statusName\":\"status name\",\"date\":\"2020-01-01T01:01:00\"}"));
    }
}