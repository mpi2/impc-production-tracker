package org.gentar.biology.project.mappers;

import org.gentar.biology.intention.ProjectIntentionCreationMapper;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.ProjectCreationDTO;
import org.gentar.biology.project.consortium.ProjectConsortium;
import org.gentar.biology.project.consortium.ProjectConsortiumMapper;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.biology.species.SpeciesMapper;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.institute.Institute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProjectCreationMapperTest
{
    private ProjectCreationMapper testInstance;

    public static final String CONSORTIUM_NAME = "Consortium Name";
    public static final String INSTITUTE_NAME = "Institute Name";
    public static String TPN = "testTpn";
    public static final long IMITS_MI_PLAN = 1L;
    public static final String PRIVACY_NAME = "testPrivacyName";
    public static final String COMMENT = "comment";
    public static final LocalDateTime REACTIVATION_DATE = LocalDateTime.of(2020, 1, 1, 1, 1);
    public static final String EXTERNAL_REFERENCE = "External reference";
    public static final boolean RECOVERY = true;

    @Mock
    private ProjectCommonDataMapper projectCommonDataMapper;

    @Mock
    private ProjectIntentionCreationMapper projectIntentionCreationMapper;

    @Mock
    private SpeciesMapper speciesMapper;
    @Mock
    private ProjectConsortiumMapper projectConsortiumMapper;

    @BeforeEach
    void setUp()
    {
        testInstance =
            new ProjectCreationMapper(
                projectCommonDataMapper,
                projectIntentionCreationMapper,
                speciesMapper,
                projectConsortiumMapper);
    }

    @Test
    void testToEntityEmptyDto()
    {
        ProjectCreationDTO projectCreationDTO = new ProjectCreationDTO();
        when(projectCommonDataMapper.toEntity(projectCreationDTO.getProjectCommonDataDTO()))
            .thenReturn(new Project());

        Project project = testInstance.toEntity(projectCreationDTO);

        assertThat(project, is(notNullValue()));
    }

    @Test
    void testToEntityDtoWithData()
    {
        ProjectCreationDTO projectCreationDTO = new ProjectCreationDTO();

        Project project = new Project();
        project.setTpn(TPN);
        project.setImitsMiPlan(IMITS_MI_PLAN);
        Privacy privacy = new Privacy();
        privacy.setName(PRIVACY_NAME);
        project.setPrivacy(privacy);
        project.setComment(COMMENT);
        project.setReactivationDate(REACTIVATION_DATE);
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

        when(projectCommonDataMapper.toEntity(projectCreationDTO.getProjectCommonDataDTO()))
            .thenReturn(project);

        Project projectCreate = testInstance.toEntity(projectCreationDTO);

        assertThat(projectCreate, is(notNullValue()));
        assertThat(projectCreate.getTpn(), is(TPN));
        assertThat(projectCreate.getImitsMiPlan(), is(IMITS_MI_PLAN));
        assertThat(projectCreate.getPrivacy().getName(), is(PRIVACY_NAME));
        assertThat(projectCreate.getComment(), is(COMMENT));
        assertThat(projectCreate.getReactivationDate(), is(REACTIVATION_DATE));
        assertThat(projectCreate.getRecovery(), is(RECOVERY));
        verify(projectCommonDataMapper, times(1))
            .toEntity(projectCreationDTO.getProjectCommonDataDTO());
    }
}