package org.gentar.biology.project;

import org.gentar.biology.project.mappers.ProjectUpdateMapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UpdateProjectRequestProcessor
{
    private final ProjectUpdateMapper projectUpdateMapper;
    private static final String NOT_MATCHING_TPN =
        "The payload contains tpn [%s] but you're modifying [%s].";

    public UpdateProjectRequestProcessor(ProjectUpdateMapper projectUpdateMapper)
    {
        this.projectUpdateMapper = projectUpdateMapper;
    }

    /**
     * Takes a project and updates it with new information from a projectDTO.
     * It updates only the information that makes sense to update by the user.
     * @param project The project without changes.
     * @param projectUpdateDTO The changes to be done in project.
     * @return a project with the new information.
     */
    public Project getProjectToUpdate(Project project, ProjectUpdateDTO projectUpdateDTO)
    {
        validateTpnIsEqual(project, projectUpdateDTO);
        Project projectToUpdate = new Project(project);
        Project mappedFromDto = projectUpdateMapper.toEntity(projectUpdateDTO);
        projectToUpdate.setComment(mappedFromDto.getComment());
        projectToUpdate.setRecovery(mappedFromDto.getRecovery());
        projectToUpdate.setEsQcOnly(mappedFromDto.getEsQcOnly());
        if (mappedFromDto.getPrivacy() != null)
        {
            projectToUpdate.setPrivacy(mappedFromDto.getPrivacy());
        }
        return projectToUpdate;
    }

    private void validateTpnIsEqual(Project project, ProjectUpdateDTO projectUpdateDTO)
    {
        String tpnInUrl = project.getTpn();
        String tpnInPayload =
            StringUtils.isEmpty(projectUpdateDTO.getTpn()) ? tpnInUrl : projectUpdateDTO.getTpn();
        if (!tpnInUrl.equals(tpnInPayload))
        {
            throw new UserOperationFailedException(
                String.format(NOT_MATCHING_TPN, tpnInPayload, tpnInUrl));
        }
    }
}
