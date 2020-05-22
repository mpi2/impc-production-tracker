package org.gentar.biology.project;

import org.gentar.biology.gene.GeneDTO;
import org.gentar.biology.gene.ProjectIntentionGeneDTO;
import org.gentar.biology.intention.ProjectIntentionDTO;
import org.gentar.biology.location.LocationDTO;
import org.gentar.biology.sequence.ProjectIntentionSequenceDTO;
import org.gentar.biology.sequence.SequenceDTO;
import org.gentar.biology.sequence.SequenceLocationDTO;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ProjectIntentionTestHelper
{
    public static void assertProjectIntentionDTOIsTheExpected(
        ProjectIntentionDTO obtained, ProjectIntentionDTO expected)
    {
        assertProjectIntentionGeneDTOIsTheExpected(
            obtained.getProjectIntentionGeneDTO(), expected.getProjectIntentionGeneDTO());

        assertProjectIntentionSequenceDTOSAreTheExpected(
            obtained.getProjectIntentionSequenceDTOS(), expected.getProjectIntentionSequenceDTOS());
       // TODO: Finish the rest of the fields
    }

    private static void assertProjectIntentionSequenceDTOSAreTheExpected(
        List<ProjectIntentionSequenceDTO> obtained,
        List<ProjectIntentionSequenceDTO> expected)
    {
        if (!obtained.isEmpty())
        {
            for (int i = 0; i < obtained.size(); i++)
            {
                ProjectIntentionSequenceDTO obtainedProjectIntentionSequenceDTO = obtained.get(i);
                ProjectIntentionSequenceDTO expectedProjectIntentionSequenceDTO = expected.get(i);
                assertThat(
                    obtainedProjectIntentionSequenceDTO.getIndex(),
                    is(expectedProjectIntentionSequenceDTO.getIndex()));
                assertSequenceDTOIsTheExpected(
                    obtainedProjectIntentionSequenceDTO.getSequenceDTO(),
                    expectedProjectIntentionSequenceDTO.getSequenceDTO());
            }
        }
    }

    private static void assertSequenceDTOIsTheExpected(SequenceDTO obtained, SequenceDTO expected)
    {
        assertThat(obtained.getSequenceCategoryName(), is(expected.getSequenceCategoryName()));
        assertThat(obtained.getSequence(), is(expected.getSequence()));
        assertThat(obtained.getSequenceTypeName(), is(expected.getSequenceTypeName()));
        assertSequenceLocationDTOSAreTheExpected(
            obtained.getSequenceLocationDTOS(), expected.getSequenceLocationDTOS());
    }

    private static void assertSequenceLocationDTOSAreTheExpected(
        List<SequenceLocationDTO> obtained, List<SequenceLocationDTO> expected)
    {
        if (!obtained.isEmpty())
        {
            for (int i = 0; i < obtained.size(); i++)
            {
                SequenceLocationDTO obtainedSequenceLocationDTO = obtained.get(i);
                SequenceLocationDTO expectedSequenceLocationDTO = expected.get(i);
                assertThat(
                    obtainedSequenceLocationDTO.getLocationIndex(),
                    is(expectedSequenceLocationDTO.getLocationIndex()));
                assertThat(
                    obtainedSequenceLocationDTO.getLocationDTO(),
                    is(expectedSequenceLocationDTO.getLocationIndex()));
                assertLocationDTOIsTheExpected(
                    obtainedSequenceLocationDTO.getLocationDTO(),
                    expectedSequenceLocationDTO.getLocationDTO());
            }
        }
    }

    private static void assertLocationDTOIsTheExpected(LocationDTO obtained, LocationDTO expected)
    {
        assertThat(obtained.getSpeciesName(), is(expected.getSpeciesName()));
        assertThat(obtained.getStrainName(), is(expected.getStrainName()));
        assertThat(obtained.getChr(), is(expected.getChr()));
        assertThat(obtained.getGenomeBuild(), is(expected.getGenomeBuild()));
        assertThat(obtained.getStart(), is(expected.getStart()));
        assertThat(obtained.getStop(), is(expected.getStop()));
        assertThat(obtained.getStrand(), is(expected.getStrand()));
    }

    public static void assertProjectIntentionGeneDTOIsTheExpected(
        ProjectIntentionGeneDTO obtained, ProjectIntentionGeneDTO expected)
    {
        assertThat(obtained.getAllOrthologs(), is(expected.getAllOrthologs()));
        assertThat(obtained.getBestOrthologs(), is(expected.getBestOrthologs()));
        assertGeneDTOIsTheExpected(obtained.getGeneDTO(), expected.getGeneDTO());
    }

    public static void assertGeneDTOIsTheExpected(GeneDTO obtained, GeneDTO expected)
    {
        assertThat(obtained.getId(), is(expected.getId()));
        assertThat(obtained.getAccId(), is(expected.getAccId()));
        assertThat(obtained.getSymbol(), is(expected.getSymbol()));
        assertThat(obtained.getName(), is(expected.getName()));
        assertThat(obtained.getExternalLink(), is(expected.getExternalLink()));
        assertThat(obtained.getSpeciesName(), is(expected.getSpeciesName()));
    }
}
