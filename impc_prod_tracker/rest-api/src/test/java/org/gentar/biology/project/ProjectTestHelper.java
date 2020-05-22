package org.gentar.biology.project;

import org.gentar.biology.intention.ProjectIntentionDTO;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ProjectTestHelper
{
    private static void assertProjectCommonDataDTOIsTheExpected(
        ProjectCommonDataDTO obtained, ProjectCommonDataDTO expected)
    {
        assertThat(obtained.getComment(), is(expected.getComment()));
        assertThat(obtained.getRecovery(), is(expected.getRecovery()));
        assertThat(obtained.getSpeciesNames(), is(expected.getSpeciesNames()));
        assertThat(obtained.getPrivacyName(), is(expected.getPrivacyName()));
        assertThat(obtained.getProjectExternalRef(), is(expected.getProjectExternalRef()));
        assertProjectConsortiumDTOSAreTheExpected(
            obtained.getProjectConsortiumDTOS(), expected.getProjectConsortiumDTOS());
    }

    private static void assertProjectConsortiumDTOSAreTheExpected(
        List<ProjectConsortiumDTO> obtained, List<ProjectConsortiumDTO> expected)
    {
        for (int i = 0; i < obtained.size(); i++)
        {
            assertThat(obtained.get(i).getConsortiumName(), is(expected.get(i).getConsortiumName()));
            assertThat(
                obtained.get(i).getProjectConsortiumInstituteNames(),
                is(expected.get(i).getProjectConsortiumInstituteNames()));
        }
    }

    public static void assertProjectResponseDTOIsTheExpected(
        ProjectResponseDTO obtained, ProjectResponseDTO expected)
    {
        assertThat(
            obtained.getTpn(), is(expected.getTpn()));
        assertThat(
            obtained.getAssignmentStatusName(), is(expected.getAssignmentStatusName()));
        assertThat(
            obtained.getSummaryStatusName(), is(expected.getSummaryStatusName()));
        assertThat(
            obtained.getImitsMiPlan(), is(expected.getImitsMiPlan()));
        assertThat(
            obtained.getIsObjectRestricted(), is(expected.getIsObjectRestricted()));
        assertThat(
            obtained.getRelatedWorkUnitNames(), is(expected.getRelatedWorkUnitNames()));
        assertThat(
            obtained.getRelatedWorkGroupNames(), is(expected.getRelatedWorkGroupNames()));
        assertProjectCommonDataDTOIsTheExpected(
            obtained.getProjectCommonDataDTO(), expected.getProjectCommonDataDTO());
        assertProjectIntentionDTOsAreTheExpected(obtained, expected);
    }

    private static void assertProjectIntentionDTOsAreTheExpected(
        ProjectResponseDTO obtained, ProjectResponseDTO expected)
    {
        List<ProjectIntentionDTO> obtainedProjectIntentionDTOs = obtained.getProjectIntentionDTOS();
        List<ProjectIntentionDTO> expectedProjectIntentionDTOs = expected.getProjectIntentionDTOS();
        for (int i = 0; i < obtainedProjectIntentionDTOs.size(); i++)
        {
            ProjectIntentionTestHelper.assertProjectIntentionDTOIsTheExpected(
                obtainedProjectIntentionDTOs.get(i), expectedProjectIntentionDTOs.get(i));
        }
    }
}
