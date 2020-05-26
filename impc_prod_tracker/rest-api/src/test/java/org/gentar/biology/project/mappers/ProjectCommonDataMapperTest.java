package org.gentar.biology.project.mappers;

import org.gentar.EntityMapper;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectCommonDataDTO;
import org.gentar.biology.project.ProjectConsortiumDTO;
import org.gentar.biology.project.consortium.ProjectConsortium;
import org.gentar.biology.project.consortium.ProjectConsortiumMapper;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.biology.species.Species;
import org.gentar.biology.species.SpeciesMapper;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.institute.Institute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProjectCommonDataMapperTest
{
    public static final String CONSORTIUM_NAME = "Consortium Name";
    public static final String INSTITUTE_NAME = "Institute Name";
    public static final String SPECIE_NAME = "SpecieName";
    public static final String PRIVACY_NAME = "testPrivacyName";
    public static final String COMMENT = "comment";
    public static final LocalDateTime REACTIVATION_DATE = LocalDateTime.of(2020, 1, 1, 1, 1);
    public static final String EXTERNAL_REFERENCE = "External reference";
    public static final boolean RECOVERY = true;
    private ProjectCommonDataMapper testInstance;

    @Autowired
    EntityMapper entityMapper;

    @Mock
    PrivacyMapper privacyMapper;

    @Mock
    SpeciesMapper speciesMapper;

    @Mock
    ProjectConsortiumMapper projectConsortiumMapper;


    @BeforeEach
    void setUp()
    {
        testInstance = new ProjectCommonDataMapper(
            entityMapper, privacyMapper, speciesMapper, projectConsortiumMapper);
    }

    @Test
    void testToDtoEmptyEntity()
    {
        Project project = new Project();
        ProjectCommonDataDTO projectDTO = testInstance.toDto(project);
        assertThat(projectDTO, is(notNullValue()));
    }

    @Test
    void testToDtoEntityWithData()
    {
        Project project = new Project();
        Privacy privacy = new Privacy();
        privacy.setName(PRIVACY_NAME);
        project.setPrivacy(privacy);
        project.setComment(COMMENT);
        project.setReactivationDate(REACTIVATION_DATE);
        project.setProjectExternalRef(EXTERNAL_REFERENCE);
        project.setRecovery(RECOVERY);
        ProjectConsortium projectConsortium = new ProjectConsortium();
        Consortium consortium = new Consortium();
        consortium.setName(CONSORTIUM_NAME);
        projectConsortium.setConsortium(consortium);
        Set<Institute> institutes = new HashSet<>();
        Institute institute = new Institute();
        institute.setName(INSTITUTE_NAME);
        institutes.add(institute);
        projectConsortium.setInstitutes(institutes);
        Set<ProjectConsortium> projectConsortia = new HashSet<>();
        projectConsortia.add(projectConsortium);
        project.setProjectConsortia(projectConsortia);
        Species species = new Species();
        species.setName(SPECIE_NAME);
        Set<Species> speciesSet = new HashSet<>();
        speciesSet.add(species);
        project.setSpecies(speciesSet);
        when(speciesMapper.toDtos(speciesSet)).thenReturn(Arrays.asList(SPECIE_NAME));

        ProjectCommonDataDTO projectDTO = testInstance.toDto(project);

        assertThat(projectDTO, is(notNullValue()));
        assertThat(projectDTO.getPrivacyName(), is(PRIVACY_NAME));
        assertThat(projectDTO.getComment(), is(COMMENT));
        assertThat(projectDTO.getProjectExternalRef(), is(EXTERNAL_REFERENCE));
        assertThat(projectDTO.getRecovery(), is(RECOVERY));
        assertThat(projectDTO.getSpeciesNames(), is(notNullValue()));
        assertThat(projectDTO.getSpeciesNames().get(0), is(SPECIE_NAME));
        verify(projectConsortiumMapper, times(1)).toDtos(projectConsortia);
    }

    @Test
    void testToEntityEmptyDto()
    {
        ProjectCommonDataDTO projectCommonDataDTO = new ProjectCommonDataDTO();
        Project project = testInstance.toEntity(projectCommonDataDTO);
        assertThat(project, is(notNullValue()));
    }

    @Test
    void testToEntityDtoWithData()
    {
        ProjectCommonDataDTO projectCommonDataDTO = new ProjectCommonDataDTO();
        projectCommonDataDTO.setPrivacyName(PRIVACY_NAME);
        projectCommonDataDTO.setComment(COMMENT);
        projectCommonDataDTO.setProjectExternalRef(EXTERNAL_REFERENCE);
        projectCommonDataDTO.setRecovery(RECOVERY);
        ProjectConsortiumDTO projectConsortiumDTO = new ProjectConsortiumDTO();
        projectConsortiumDTO.setConsortiumName(CONSORTIUM_NAME);
        projectConsortiumDTO.setInstituteNames(Arrays.asList(INSTITUTE_NAME));
        List<ProjectConsortiumDTO> projectConsortiumDTOS = new ArrayList<>();
        projectConsortiumDTOS.add(projectConsortiumDTO);
        projectCommonDataDTO.setProjectConsortiumDTOS(projectConsortiumDTOS);
        Privacy privacy = new Privacy();
        privacy.setName(PRIVACY_NAME);
        when(privacyMapper.toEntity(PRIVACY_NAME)).thenReturn(privacy);

        Project project = testInstance.toEntity(projectCommonDataDTO);

        assertThat(project, is(notNullValue()));
        assertThat(project.getPrivacy().getName(), is(PRIVACY_NAME));
        assertThat(project.getComment(), is(COMMENT));
        assertThat(project.getProjectExternalRef(), is(EXTERNAL_REFERENCE));
        assertThat(project.getRecovery(), is(RECOVERY));
        verify(projectConsortiumMapper, times(1)).toEntities(projectConsortiumDTOS);
    }
}